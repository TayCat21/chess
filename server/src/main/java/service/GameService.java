package service;

import dataaccess.*;
import model.*;

public class GameService {
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;

    public GameService(AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws DataAccessException {

        Authdata existingToken = authDataAccess.getAuth(authToken);
        if (existingToken == null) {
            throw new DataAccessException("unauthorized");
        }

        int gameID = gameDataAccess.createGame(createGameRequest.gameName());

        return new CreateGameResult(gameID);
    }

    public void clear() {
        gameDataAccess.clear();
    }
}
