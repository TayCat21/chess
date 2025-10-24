package service;

import dataaccess.*;
import model.*;
import java.util.UUID;

public class UserService {
	MemoryUserDataAccess userDataAccess;
	MemoryAuthDataAccess authDataAccess;

	public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {

		Userdata existingUser = userDataAccess.getUser(registerRequest.username());
		if (existingUser != null) {
			throw new DataAccessException("Username Already Taken");
		}

		userDataAccess.addUser(registerRequest.username(), registerRequest.password(), registerRequest.email());
		String authToken = UUID.randomUUID().toString();
		authDataAccess.makeAuth(authToken, registerRequest.username());

		return new RegisterResult(authToken, registerRequest.username());

	}

//	public LoginResult login(LoginRequest loginRequest) {
//		return LoginResult;
//	}
//
//	public void logout(LogoutRequest logoutRequest) {}
}
