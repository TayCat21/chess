package service;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private GameService gameService;
    private MemoryAuthDataAccess authDataAccess;
    private MemoryGameDataAccess gameDataAccess;
    private String validAuthToken;

    @BeforeEach
    void setup() {
        authDataAccess = new MemoryAuthDataAccess();
        gameDataAccess = new MemoryGameDataAccess();
        gameService = new GameService(authDataAccess, gameDataAccess);

        validAuthToken = "valid-auth";
        authDataAccess.makeAuth(validAuthToken, "myUsername");
    }

    @Test
    void createGame_positive() throws DataAccessException {
        CreateGameRequest req = new CreateGameRequest("myGameName");
        CreateGameResult result = gameService.createGame(validAuthToken, req);
        assertNotNull(result);
        assertTrue(result.gameID() > 0, "Game ID should be 1+");
    }

    @Test
    void createGame_invalidToken() {
        CreateGameRequest req = new CreateGameRequest("myGameName");
        assertThrows(DataAccessException.class, () ->
                gameService.createGame("badToken", req));
    }

    @Test
    void listGames_positive() throws DataAccessException {
        CreateGameRequest req = new CreateGameRequest("myListGame");
        gameService.createGame(validAuthToken, req);

        ListGamesResult result = gameService.listGames(validAuthToken);
        assertNotNull(result);
        assertFalse(result.games().isEmpty(), "Should have myListGame");
        assertEquals("myListGame", result.games().getFirst().gameName());
    }

    @Test
    void listGames_invalidToken() {
        assertThrows(DataAccessException.class, () ->
                gameService.listGames("badToken"));
    }

    @Test
    void joinGame_positive() throws DataAccessException {
        int gameId = gameService.createGame(validAuthToken, new CreateGameRequest("myJoinGame")).gameID();
        JoinGameRequest joinRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameId);
        assertDoesNotThrow(() -> gameService.joinGame(validAuthToken, joinRequest));
    }

    @Test
    void joinGame_invalidToken() {
        JoinGameRequest joinRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK, 1);
        assertThrows(DataAccessException.class, () ->
                gameService.joinGame("badToken", joinRequest));
    }

    @Test
    void clear() throws DataAccessException {
        gameService.createGame(validAuthToken, new CreateGameRequest("myClearGame"));
        gameService.clear();

        ListGamesResult result = gameService.listGames(validAuthToken);
        assertTrue(result.games().isEmpty(), "All games should be cleared");
    }

    @Test
    void authenticateToken_positive() {
        assertDoesNotThrow(() -> gameService.authenticateToken(validAuthToken));
    }

    @Test
    void authenticateToken_invalidToken() {
        assertThrows(DataAccessException.class, () ->
                gameService.authenticateToken("badToken"));
    }
}