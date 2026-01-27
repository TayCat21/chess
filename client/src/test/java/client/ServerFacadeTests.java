package client;

import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        facade = new ServerFacade("http://localhost:8080");

        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void clearDatabase() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerPositive() throws Exception {
        facade.register("userA", "passA", "emailA@email.com");
        assertNotNull(facade.getUserAuth());
    }

    @Test
    void registerNegative() {
        assertThrows(ClientException.class, () -> {
            facade.register("userB", "passB", "emailB@email.com");
            facade.register("userB", "passA", "emailA@email.com");
        });
    }

    @Test
    void loginPositive() throws Exception {
        facade.register("userC", "passC", "emailC@email.com");
        facade.login("userC", "passC");
        assertNotNull(facade.getUserAuth());
    }

    @Test
    void loginNegative() throws Exception {
        facade.register("badUser", "pass", "email@email.com");
        assertThrows(ClientException.class, () ->
                facade.login("badUser", "wrongPass"));
    }

    @Test
    void logoutPositive() throws Exception {
        facade.register("userD", "passD", "emailD@email.com");
        facade.logout();
        assertNull(facade.getUserAuth());
    }

    @Test
    void logoutNegative() {
        assertThrows(ClientException.class, () -> facade.logout());
    }

    @Test
    void listGamesPositive() throws Exception {
        facade.register("userE", "passE", "emailE@email.com");
        facade.listGames();
        assertNotNull(facade.getUpdatedGames());
    }

    @Test
    void listGamesNegative() {
        assertThrows(ClientException.class, () -> facade.listGames());
    }

    @Test
    void createGamePositive() throws Exception {
        facade.register("userF", "passF", "emailF@email.com");
        facade.createGame("testGame");
        assertTrue(facade.getListSize() >= 0);
    }

    @Test
    void createGameNegative() {
        assertThrows(ClientException.class, () ->
                facade.createGame("unauthorizedGame"));
    }

    @Test
    void joinGamePositive() throws Exception {
        facade.register("userG", "passG", "emailG@email.com");
        facade.createGame("testGame2");
        facade.listGames();

        int gameID = facade.getUpdatedGames().get(0).gameID();

        facade.joinGame(gameID, "WHITE");
        assertTrue(true);
    }

    @Test
    void joinGameNegative() throws Exception {
        facade.register("userH", "passH", "emailH@email.com");
        facade.createGame("takenGame");
        facade.listGames();

        int gameID = facade.getUpdatedGames().get(0).gameID();
        facade.joinGame(gameID, "WHITE");

        assertThrows(ClientException.class, () ->
                facade.joinGame(gameID, "WHITE"));
    }

    @Test
    void getListSizePositive() throws Exception {
        facade.register("listSizeUser", "pass", "e@e.com");
        facade.createGame("game1");
        facade.listGames();
        assertTrue(facade.getListSize() >= 1);
    }

    @Test
    void getUpdatedGamesNegative() {
        assertNotNull(facade.getUpdatedGames());
    }

}
