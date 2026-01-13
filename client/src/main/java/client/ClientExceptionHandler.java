package client;

import com.google.gson.Gson;
import io.javalin.http.Context;

import java.util.Map;

public class ClientExceptionHandler {

    public ClientException.Code exceptions(Exception e) {
        int status = 500;
        String message = "Error: " + e.getMessage();

        if (e.getMessage().equals("bad request")) {
            status = 400;
        } else if (e.getMessage().equals("unauthorized")) {
            status = 401;
        } else if (e.getMessage().equals("already taken")) {
            status = 403;
        }

        var body = new Gson().toJson(Map.of("message", message, "success", false));
        context.status(status).json(body);
    }
}
