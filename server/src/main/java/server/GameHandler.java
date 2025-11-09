package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.*;
import dataaccess.*;

public class GameHandler {

    GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void createGame(@NotNull Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        authenticateToken(authToken);

        var serializer = new Gson();
        CreateGameRequest requestBody;
        try {
            requestBody = serializer.fromJson(context.body(), CreateGameRequest.class);
        } catch (Exception e) {
            throw new DataAccessException("bad request");
        }

        if (requestBody == null || requestBody.gameName() == null || requestBody.gameName().isEmpty()) {
            throw new DataAccessException("bad request");
        }

        CreateGameResult resultBody = gameService.createGame(authToken, requestBody);
        ResponseUtil.success(context, resultBody);
    }

    public void listGames(@NotNull Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        authenticateToken(authToken);

        ListGamesResult resultBody = gameService.listGames(authToken);
        ResponseUtil.success(context, resultBody);
    }

    public void joinGame(@NotNull Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        authenticateToken(authToken);


        var serializer = new Gson();
        JoinGameRequest requestBody;
        try {
            requestBody = serializer.fromJson(context.body(), JoinGameRequest.class);
        } catch (Exception e) {
            throw new DataAccessException("bad request");
        }

        if (requestBody == null || requestBody.playerColor() == null) {
            throw new DataAccessException("bad request");
        }

        gameService.joinGame(authToken, requestBody);
        ResponseUtil.success(context);
    }


    public void authenticateToken(String authToken) throws DataAccessException {
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }
    }
}