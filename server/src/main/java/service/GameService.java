package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;

import java.util.List;

public class GameService {
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;

    public GameService(AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws DataAccessException {
        authenticateToken(authToken);
        int gameID = gameDataAccess.createGame(createGameRequest.gameName());

        return new CreateGameResult(gameID);
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException {
        authenticateToken(authToken);
        List<ListGamesItem> gamesList = gameDataAccess.listGames();

        return new ListGamesResult(gamesList);
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws DataAccessException {
        authenticateToken(authToken);
        String username = authDataAccess.getAuth(authToken).username();
        gameDataAccess.updateGame(joinGameRequest.playerColor(), username, joinGameRequest.gameID());
    }

    public GetBoardResult getBoard(String authToken, GetBoardRequest getBoardRequest) throws DataAccessException {
        authenticateToken(authToken);
        Gamedata game = gameDataAccess.getGame(getBoardRequest.gameID());
        return new GetBoardResult(game);
    }

    public void clear() {
        try {
            gameDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public void authenticateToken(String authToken) throws DataAccessException {
        Authdata existingToken = authDataAccess.getAuth(authToken);
        if (existingToken == null) {
            throw new DataAccessException("unauthorized");
        }
    }
}
