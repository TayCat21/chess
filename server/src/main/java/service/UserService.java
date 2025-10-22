package service;

import model.*;

public class UserService {

    public model.Userdata getUser(String username) {
        // call DAO?
        return userdata;
    }

    public model.Authdata addUser(Userdata userdata) {

        return authdata;
    }
}

/*
public class UserService {
	public RegisterResult register(RegisterRequest registerRequest) {}
	public LoginResult login(LoginRequest loginRequest) {}
	public void logout(LogoutRequest logoutRequest) {}
}
 */
