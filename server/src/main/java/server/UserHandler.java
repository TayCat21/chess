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

    public void register(@NotNull Context context) {
        Userdata userdata = new Gson().fromJson(context.body(), Userdata.class);

        if (userdata.getUsername() == null || userdata.getPassword() == null) {
            throw new BadRequestResponse("Username or Password returned null");
        }

        try {
            Authdata authdata = userService.addUser(userdata);
        }
    }
}
