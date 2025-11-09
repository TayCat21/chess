package service;

import dataaccess.*;
import model.*;
import java.util.UUID;

public class UserService {
	private final AuthDataAccess authDataAccess;
	private final UserDataAccess userDataAccess;

	public UserService(AuthDataAccess authDataAccess, UserDataAccess userDataAccess) {
		this.authDataAccess = authDataAccess;
		this.userDataAccess = userDataAccess;
	}

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

	public void logout(LogoutRequest logoutRequest) throws DataAccessException {
		Authdata existingToken = authDataAccess.getAuth(logoutRequest.authToken());
		if (existingToken == null) {
			throw new DataAccessException("unauthorized");
		}

		authDataAccess.deleteAuth(logoutRequest.authToken());
	}

	public void clear() {
		userDataAccess.clear();
		authDataAccess.clear();
	}
}
