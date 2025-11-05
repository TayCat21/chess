package server;

import io.javalin.http.Context;

public class ResponseUtil {

    public static void success(Context context, Object result) {
        context.status(200);
        context.json(result);
    }

    public static void success(Context context) {
        context.status(200);
    }

}
