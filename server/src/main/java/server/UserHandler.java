package server;

import com.google.gson.Gson;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import model.Authdata;
import model.Userdata;
import org.jetbrains.annotations.NotNull;
import service.UserService;

public class UserHandler {

    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    //register(RegisterRequest) Handler --> Service
    public void register(@NotNull Context context) {
        Userdata requestBody = new Gson().fromJson(context.body(), Userdata.class);

        if (requestBody.username() == null || requestBody.password() == null) {
            throw new BadRequestResponse("Username or Password returned null");
        }

        // getUser(username) Service --> Data Access
        Userdata getUserResult = userService.getUser(requestBody.username());


    }
}
