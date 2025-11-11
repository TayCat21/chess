package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.*;

public class UserHandler {

    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    //register(RegisterRequest) Handler --> Service
    public void register(@NotNull Context context) throws DataAccessException {
        var serializer = new Gson();
        RegisterRequest requestBody = serializer.fromJson(context.body(), RegisterRequest.class);

        if (requestBody.username() == null || requestBody.password() == null) {
            throw new DataAccessException("bad request");
        }

        RegisterResult resultBody = userService.register(requestBody);
        var json = serializer.toJson(resultBody);
        ResponseUtil.success(context, json);
    }

    public void login(@NotNull Context context) throws DataAccessException {
        var serializer = new Gson();
        LoginRequest requestBody = serializer.fromJson(context.body(), LoginRequest.class);

        if (requestBody.username() == null || requestBody.password() == null) {
            throw new DataAccessException("bad request");
        }

        LoginResult resultBody = userService.login(requestBody);
        var json = serializer.toJson(resultBody);
        ResponseUtil.success(context, json);
    }

    public void logout(@NotNull Context context) throws DataAccessException {
        String authToken = context.header("authorization");

        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }

        LogoutRequest request = new LogoutRequest(authToken);

        userService.logout(request);
        ResponseUtil.success(context);
    }
}
