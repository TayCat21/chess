package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import model.Authdata;

import java.util.UUID;

public class GameService {
    MemoryAuthDataAccess authDataAccess = new MemoryAuthDataAccess();

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws DataAccessException {

        Authdata existingToken = authDataAccess.getAuth(authToken);
        if (existingToken == null) {
            throw new DataAccessException("unauthorized");
        }

        String gameID = UUID.randomUUID().toString();
        authDataAccess.makeAuth(authToken, loginRequest.username());

        return new LoginResult(loginRequest.username(), authToken);
    }
}
