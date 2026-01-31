package server;

import chess.ChessGame;
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
            PlayGameCommand play = new Gson().fromJson(ctx.message(), PlayGameCommand.class);
            switch (action.getCommandType()) {
                case CONNECT -> connect(action.getAuthToken(), action.getGameID(), ctx.session);
                case CONNECT_PLAYER -> connectPlayer(play.getAuthToken(), play.getGameID(),
                        play.getColor(), ctx.session);
                case MAKE_MOVE -> makeMove(action.getAuthToken(), action.getGameID(), ctx.session);
                case LEAVE -> leaveGame(action.getAuthToken(), action.getGameID(), ctx.session);
                case RESIGN -> resign(action.getAuthToken(), action.getGameID(), ctx.session);
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

    private void makeMove(String authToken, int gameID, Session session) throws IOException {}

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

    private void resign(String authToken, int gameID, Session session) throws IOException {}

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
        String msg = notification.toString();
        for (Session c : connections.getSessions(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(thisSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void broadcastAll(int gameID, NotificationMsg notification) throws IOException {
        String msg = notification.toString();
        for (Session c : connections.getSessions(gameID)) {
            if (c.isOpen()) {
                c.getRemote().sendString(msg);
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
