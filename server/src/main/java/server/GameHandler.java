package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.Authdata;
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

        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }

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
}