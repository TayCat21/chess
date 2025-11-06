package server;

import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;
import service.GameService;

public class Server {

    private final Javalin javalin;
    UserService userService = new UserService();
    UserHandler userHandler = new UserHandler(userService);
    GameService gameService = new GameService();
    GameHandler gameHandler = new GameHandler(gameService);
    ExceptionHandler exceptionHandler = new ExceptionHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Server --> Handler
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        javalin.post("/game", gameHandler::createGame);

        javalin.delete("/db", this::clear);

        javalin.exception(Exception.class, exceptionHandler::exceptions);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public void clear(Context context) {
        userService.clear();
        //gameService.clear();
        ResponseUtil.success(context);
    }
}
