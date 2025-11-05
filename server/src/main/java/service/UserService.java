package service;

import dataaccess.*;
import model.*;
import java.util.UUID;

public class UserService {
	MemoryUserDataAccess userDataAccess = new MemoryUserDataAccess();
	MemoryAuthDataAccess authDataAccess = new MemoryAuthDataAccess();

	public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {

		Userdata existingUser = userDataAccess.getUser(registerRequest.username());
		if (existingUser != null) {
			throw new DataAccessException("already taken");
		}

		userDataAccess.addUser(registerRequest.username(), registerRequest.password(), registerRequest.email());
		String authToken = UUID.randomUUID().toString();
		authDataAccess.makeAuth(authToken, registerRequest.username());

		return new RegisterResult(registerRequest.username(), authToken);

	}

	public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
		Userdata existingUser = userDataAccess.getUser(loginRequest.username());
		if (existingUser == null) {
			throw new DataAccessException("unauthorized");
		}

		String authToken = UUID.randomUUID().toString();
		authDataAccess.makeAuth(authToken, loginRequest.username());

		return new LoginResult(loginRequest.username(), authToken);
	}
//
//	public void logout(LogoutRequest logoutRequest) {}
}
