package service;

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

    public void clear() {
        gameDataAccess.clear();
    }


    public void authenticateToken(String authToken) throws DataAccessException {
        Authdata existingToken = authDataAccess.getAuth(authToken);
        if (existingToken == null) {
            throw new DataAccessException("unauthorized");
        }
    }
}
