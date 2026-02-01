package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.websocket.*;
import model.Authdata;
import model.Gamedata;
import websocket.commands.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;
import java.io.IOException;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final WebsocketSessions connections = new WebsocketSessions();
    private final AuthDataAccess authDAO;
    private final GameDataAccess gameDAO;

    public WebsocketHandler(AuthDataAccess authDAO, GameDataAccess gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand action = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            PlayGameCommand play;
            MakeMoveCommand moves;

            switch (action.getCommandType()) {
                case CONNECT -> connect(action.getAuthToken(), action.getGameID(), ctx.session);
                case CONNECT_PLAYER -> {
                        play = new Gson().fromJson(ctx.message(), PlayGameCommand.class);
                        connectPlayer(play.getAuthToken(), play.getGameID(),
                        play.getColor(), ctx.session);
                        }
                case MAKE_MOVE -> {
                    moves = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
                    makeMove(moves.getAuthToken(), moves.getGameID(), moves.getColor(),
                            moves.getMove(), moves, ctx.session);
                }
                case LEAVE -> leaveGame(action.getAuthToken(), action.getGameID(), ctx.session);
                case RESIGN -> {
                    play = new Gson().fromJson(ctx.message(), PlayGameCommand.class);
                    resign(play.getAuthToken(), play.getGameID(), play.getColor(), ctx.session);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(String authToken, int gameID, Session session) throws IOException {
        try {
            Authdata auth = authenticate(authToken);
            Gamedata game = gameDAO.getGame(gameID);

            connections.addSession(gameID, session);
            var message = String.format("%s joined the game as an observer", auth.username());
            var notification = new NotificationMsg(message);
            broadcastMsg(session, gameID, notification);
            LoadGame loadGame = new LoadGame(game.getGame(), ChessGame.TeamColor.WHITE);
            sendMsg(session, loadGame);

        } catch (UnauthorizedResponse e) {
        sendError(session, new ErrorMsg("Error: not authorized"));
    } catch (DataAccessException e) {
        sendError(session, new ErrorMsg("Error: game not valid"));
    }
    }

    private void connectPlayer(String authToken, int gameID, ChessGame.TeamColor color,
                               Session session) throws IOException {
        try {
            Authdata auth = authenticate(authToken);
            Gamedata game = gameDAO.getGame(gameID);

            connections.addSession(gameID, session);
            String storedColorUser = (color == ChessGame.TeamColor.WHITE) ?
                    game.getWhiteUsername() : game.getBlackUsername();
            boolean trueUserColor = auth.username().equals(storedColorUser);
            if (!trueUserColor) {
                ErrorMsg error = new ErrorMsg("Error: join attempted with wrong color");
                sendError(session, error);
                return;
            }

            connections.addSession(gameID, session);
            var message = String.format("%s joined the game as %s", auth.username(), color.toString());
            var notification = new NotificationMsg(message);
            broadcastMsg(session, gameID, notification);
            LoadGame loadGame = new LoadGame(game.getGame(), color);
            sendMsg(session, loadGame);

        } catch (UnauthorizedResponse e) {
            sendError(session, new ErrorMsg("Error: not authorized"));
        } catch (DataAccessException e) {
            sendError(session, new ErrorMsg("Error: game not valid"));
        }
    }

    private void makeMove(String authToken, int gameID, ChessGame.TeamColor myColor,
                          ChessMove move, MakeMoveCommand command, Session session) throws IOException {
        try {
            Authdata auth = authenticate(authToken);
            Gamedata game = gameDAO.getGame(gameID);

            if(game.game().getGameOver()) {
                sendError(session, new ErrorMsg("Error: Game is over, no moves available"));
                return;
            }

            if(game.game().getTeamTurn().equals(myColor)) {
                game.game().makeMove(command.getMove());

                NotificationMsg notification;
                ChessGame.TeamColor opColor = (myColor == ChessGame.TeamColor.WHITE)?
                        ChessGame.TeamColor.BLACK: ChessGame.TeamColor.WHITE;

                if (game.game().isInCheck(opColor)) {
                    var message = String.format("%s just placed %s in check!", auth.username(), opColor.toString());
                    notification = new NotificationMsg(message);
                }
                else if (game.game().isInCheckmate(opColor)) {
                    var message = String.format("%s just placed won Checkmate!", auth.username());
                    notification = new NotificationMsg(message);
                }
                else if (game.game().isInStalemate(opColor)) {
                    var message = String.format("Tie! %s just caused a stalemate!", auth.username());
                    notification = new NotificationMsg(message);
                }
                else {
                    var message = String.format("%s just made a move", auth.username());
                    notification = new NotificationMsg(message);
                }
                broadcastMsg(session, gameID, notification);
                LoadGame loadGame = new LoadGame(game.getGame(), myColor);
                sendMsg(session, loadGame);
            }
            else {
                sendError(session, new ErrorMsg("Error: it is the other team's turn"));
            }

        } catch (UnauthorizedResponse e) {
            sendError(session, new ErrorMsg("Error: not authorized"));
        } catch (DataAccessException e) {
            sendError(session, new ErrorMsg("Error: game not valid"));
        } catch (InvalidMoveException e) {
            sendError(session, new ErrorMsg("Error: invalid move"));
        }
    }

    private void leaveGame(String authToken, int gameID, Session session) throws IOException {
        try{
            Authdata auth = authenticate(authToken);
            connections.removeSession(gameID, session);
            var message = String.format("%s has left the game", auth.username());
            var notification = new NotificationMsg(message);
            broadcastMsg(session, gameID, notification);

        } catch (UnauthorizedResponse e) {
            sendError(session, new ErrorMsg("Error: not authorized"));
        }
    }

    private void resign(String authToken, int gameID, ChessGame.TeamColor resignColor, Session session)
            throws IOException {
        try {
            Authdata auth = authenticate(authToken);
            Gamedata game = gameDAO.getGame(gameID);

            String winningUser = (resignColor == ChessGame.TeamColor.WHITE) ? game.blackUsername() :
                    game.whiteUsername();

            if (game.game().getGameOver()) {
                sendError(session, new ErrorMsg("Error: Game is already over"));
                return;
            }
            game.game().setGameOver(true);

            var message = String.format("%s forfeits the game. %s wins!", auth.username(), winningUser);
            var notification = new NotificationMsg(message);
            broadcastAll(gameID, notification);

        } catch (UnauthorizedResponse e) {
            sendError(session, new ErrorMsg("Error: not authorized"));
        } catch (DataAccessException e) {
            sendError(session, new ErrorMsg("Error: game not valid"));
        }
    }

    private Authdata authenticate(String authToken) {
        try {
            Authdata auth = authDAO.getAuth(authToken);
            if (auth == null) {
                throw new UnauthorizedResponse("unauthorized");
            }
            return auth;
        } catch (Exception e) {
            throw new UnauthorizedResponse("unauthorized");
        }
    }

    public void broadcastMsg(Session thisSession, int gameID, NotificationMsg notification) throws IOException {
        for (Session c : connections.getSessions(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(thisSession)) {
                    sendMsg(c, notification);
                }
            }
        }
    }

    public void broadcastAll(int gameID, NotificationMsg notification) throws IOException {
        for (Session c : connections.getSessions(gameID)) {
            if (c.isOpen()) {
                sendMsg(c, notification);
            }
        }
    }

    private void sendError(Session session, ErrorMsg error) throws IOException {
        String message = new Gson().toJson(error);
        session.getRemote().sendString(message);
    }

    private void sendMsg(Session session, ServerMessage msg) throws IOException {
        String message = new Gson().toJson(msg);
        session.getRemote().sendString(message);
    }

}
