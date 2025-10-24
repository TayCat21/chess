package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;
    UserHandler userHandler;
    ExceptionHandler exceptionHandler;

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
