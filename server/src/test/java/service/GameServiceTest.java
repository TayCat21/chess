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
    void createGamePositive() throws DataAccessException {
        CreateGameRequest req = new CreateGameRequest("myGameName");
        CreateGameResult result = gameService.createGame(validAuthToken, req);
        assertNotNull(result);
        assertTrue(result.gameID() > 0, "Game ID should be 1+");
    }

    @Test
    void createGameInvalidToken() {
        CreateGameRequest req = new CreateGameRequest("myGameName");
        assertThrows(DataAccessException.class, () ->
                gameService.createGame("badToken", req));
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        CreateGameRequest req = new CreateGameRequest("myListGame");
        gameService.createGame(validAuthToken, req);

        ListGamesResult result = gameService.listGames(validAuthToken);
        assertNotNull(result);
        assertFalse(result.games().isEmpty(), "Should have myListGame");
        assertEquals("myListGame", result.games().getFirst().gameName());
    }

    @Test
    void listGamesInvalidToken() {
        assertThrows(DataAccessException.class, () ->
                gameService.listGames("badToken"));
    }

    @Test
    void joinGamePositive() throws DataAccessException {
        int gameId = gameService.createGame(validAuthToken, new CreateGameRequest("myJoinGame")).gameID();
        JoinGameRequest joinRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameId);
        assertDoesNotThrow(() -> gameService.joinGame(validAuthToken, joinRequest));
    }

    @Test
    void joinGameInvalidToken() {
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
    void authenticateTokenPositive() {
        assertDoesNotThrow(() -> gameService.authenticateToken(validAuthToken));
    }

    @Test
    void authenticateTokenInvalidToken() {
        assertThrows(DataAccessException.class, () ->
                gameService.authenticateToken("badToken"));
    }
}