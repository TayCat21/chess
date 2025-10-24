package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import java.util.Map;

public class ExceptionHandler {
    public void exceptions(Exception e, Context context) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        context.status(500);
        context.json(body);
    }
}
