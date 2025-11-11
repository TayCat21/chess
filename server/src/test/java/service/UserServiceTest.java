package service;

import dataaccess.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private MemoryAuthDataAccess authDataAccess;
    private MemoryUserDataAccess userDataAccess;

    @BeforeEach
    void setup() {
        authDataAccess = new MemoryAuthDataAccess();
        userDataAccess = new MemoryUserDataAccess();
        userService = new UserService(authDataAccess, userDataAccess);
    }


    @Test
    void register_positive() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("myUsername", "myPassword", "email@fakemail.com");
        RegisterResult res = userService.register(req);

        assertNotNull(res);
        assertEquals("myUsername", res.username());
        assertNotNull(res.authToken());
        assertNotNull(authDataAccess.getAuth(res.authToken()), "authToken should exist");
        assertNotNull(userDataAccess.getUser("myUsername"), "username should be stored");
    }

    @Test
    void register_AlreadyTaken() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("bob", "pass", "bob@example.com");
        userService.register(req);

        DataAccessException e = assertThrows(DataAccessException.class, () ->
                userService.register(req)
        );
        assertTrue(e.getMessage().toLowerCase().contains("taken"));
    }

    @Test
    void login_positive() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("anotherUser", "pass", "user@fakemail.com");
        userService.register(req);

        LoginRequest login = new LoginRequest("anotherUser", "pass");
        LoginResult res = userService.login(login);

        assertEquals("anotherUser", res.username());
        assertNotNull(authDataAccess.getAuth(res.authToken()));
    }

    @Test
    void login_WrongPass() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("user123", "mine", "user123@fakemail.com");
        userService.register(req);

        LoginRequest badLogin = new LoginRequest("user123", "notMine");
        DataAccessException e = assertThrows(DataAccessException.class, () ->
                userService.login(badLogin)
        );
        assertTrue(e.getMessage().toLowerCase().contains("unauthorized"));
    }

    @Test
    void login_NoUser() {
        LoginRequest badLogin = new LoginRequest("notUser", "notPass");
        DataAccessException e = assertThrows(DataAccessException.class, () ->
                userService.login(badLogin)
        );
        assertTrue(e.getMessage().toLowerCase().contains("unauthorized"));
    }

    @Test
    void logout_positive() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("myName", "myPass", "kitty@fakemail.com");
        RegisterResult res = userService.register(req);

        LogoutRequest logoutReq = new LogoutRequest(res.authToken());
        userService.logout(logoutReq);

        assertNull(authDataAccess.getAuth(res.authToken()), "authToken should be removed");
    }

    @Test
    void logout_InvalidToken() {
        LogoutRequest badLogout = new LogoutRequest("badToken");

        DataAccessException e = assertThrows(DataAccessException.class, () ->
                userService.logout(badLogout)
        );
        assertTrue(e.getMessage().toLowerCase().contains("unauthorized"));
    }

    @Test
    void clear() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("username", "password", "email@fakemail.com");
        RegisterResult res = userService.register(req);

        userService.clear();
        assertNull(userDataAccess.getUser("username"), "All users should be cleared");
        assertNull(authDataAccess.getAuth(res.authToken()), "all authTokens should be cleared");
    }

    @Test
    void clearEmpty() {
        assertDoesNotThrow(() -> userService.clear());
    }
}