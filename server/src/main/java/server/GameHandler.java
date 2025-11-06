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
        CreateGameRequest requestBody = serializer.fromJson(context.body(), CreateGameRequest.class);

        if (requestBody.gameName() == null) {
            throw new DataAccessException("bad request");
        }

        CreateGameResult resultBody = gameService.createGame(authToken, requestBody);
        ResponseUtil.success(context, resultBody);
    }
}