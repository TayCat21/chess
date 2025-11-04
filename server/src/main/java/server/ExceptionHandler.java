package server;

import com.google.gson.Gson;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.http.Context;
import java.util.Map;

public class ExceptionHandler {

    public void exceptions(Exception e, Context context) {
        int status = 500;
        String message = "Error: " + e.getMessage();

        if (e instanceof BadRequestResponse) {
            status = 400;
            message = "Error: Bad request";
        } else if (e instanceof UnauthorizedResponse) {
            status = 401;
            message = "Error: Unauthorized";
        } else if (e instanceof ForbiddenResponse) {
            status = 403;
            message = "Error: already taken";
        }

        var body = new Gson().toJson(Map.of("message", message, "success", false));
        context.status(status).json(body);
    }
}
