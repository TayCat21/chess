package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import model.Userdata;
import org.jetbrains.annotations.NotNull;
import service.*;

public class UserHandler {

    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    //register(RegisterRequest) Handler --> Service
    public String register(@NotNull Context context) throws DataAccessException {
        var serializer = new Gson();
        RegisterRequest requestBody = serializer.fromJson(context.body(), RegisterRequest.class);

        if (requestBody.username() == null || requestBody.password() == null) {
            throw new BadRequestResponse("Username or Password returned null");
        }

         RegisterResult resultBody = userService.register(requestBody);

        return serializer.toJson(resultBody);
    }
}
