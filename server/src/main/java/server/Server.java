package server;

import io.javalin.*;
import service.UserService;

public class Server {

    private final Javalin javalin;
    UserService userService = new UserService();
    UserHandler userHandler = new UserHandler(userService);
    ExceptionHandler exceptionHandler = new ExceptionHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Server --> Handler
        javalin.post("/user", userHandler::register);

        javalin.exception(Exception.class, exceptionHandler::exceptions);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
