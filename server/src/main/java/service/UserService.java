package service;

import dataaccess.DataAccessException;
import dataaccess.*;
import io.javalin.http.BadRequestResponse;
import kotlin.io.AccessDeniedException;
import model.Userdata;

public class UserService {
	MemoryUserDataAccess userDataAccess;

	public RegisterResult register(RegisterRequest registerRequest) {
		try {
			Userdata existingUser = userDataAccess.getUser(registerRequest.username());
			throw new DataAccessException("Username Already Taken");
		}
		catch(DataAccessException e) {

		}

		return
	};

//	public LoginResult login(LoginRequest loginRequest) {
//		return LoginResult;
//	};
//
//	public void logout(LogoutRequest logoutRequest) {}
}
