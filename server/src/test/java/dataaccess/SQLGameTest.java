package dataaccess;

import chess.ChessGame;
import model.Gamedata;
import org.junit.jupiter.api.*;
import service.ListGamesItem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameTest {

    private SQLGame sqlGame;

    @BeforeEach
    public void setup() throws Exception {
        sqlGame = new SQLGame();
        sqlGame.clear();
    }

    @Test
    public void createGamePositive() throws DataAccessException {
        int myID = sqlGame.createGame("MyGame");
        assertTrue(myID > 0);
        assertTrue(sqlGame.gameExists(myID));
    }

    @Test
    public void testCreateGameNegative() {
        assertThrows(DataAccessException.class, () -> {
            sqlGame.createGame(null);
        });
    }

    @Test
    public void getGamePositive() throws DataAccessException {
        int myID = sqlGame.createGame("myGame1");
        Gamedata game = sqlGame.getGame(myID);

        assertNotNull(game);
        assertEquals(myID, game.gameID());
        assertEquals("myGame1", game.gameName());
    }

    @Test
    public void getGameNegative() {
        Gamedata result = sqlGame.getGame(1234);
        assertNull(result);
    }

    @Test
    public void listGamesPositive() throws DataAccessException {
        sqlGame.createGame("gameA");
        sqlGame.createGame("gameB");
        List<ListGamesItem> myGames = sqlGame.listGames();

        assertEquals(2, myGames.size());
        assertEquals("gameA", myGames.get(0).gameName());
        assertEquals("gameB", myGames.get(1).gameName());
    }

    @Test
    public void listGamesNegative() throws DataAccessException {
        List<ListGamesItem> myGames = sqlGame.listGames();
        assertTrue(myGames.isEmpty());
    }

    @Test
    public void updateGamePositive() throws DataAccessException {
        int myID = sqlGame.createGame("myGame2");
        sqlGame.updateGame(ChessGame.TeamColor.WHITE, "user1", myID);
        Gamedata updated = sqlGame.getGame(myID);

        assertNotNull(updated);
        assertEquals("user1", updated.whiteUsername());
    }

    @Test
    public void updateGameNegative() throws DataAccessException {
        int myID = sqlGame.createGame("myGame3");
        sqlGame.updateGame(ChessGame.TeamColor.WHITE, "userA", myID);
        assertThrows(DataAccessException.class, () -> {
            sqlGame.updateGame(ChessGame.TeamColor.WHITE, "userB", myID);
        });
    }

    @Test
    public void gameSpotOpenPositive() throws DataAccessException {
        int myID = sqlGame.createGame("myGame4");
        assertTrue(sqlGame.gameSpotOpen(myID, ChessGame.TeamColor.WHITE));
    }

    @Test
    public void gameSpotOpenNegative() throws DataAccessException {
        int myID = sqlGame.createGame("myGame5");
        sqlGame.updateGame(ChessGame.TeamColor.WHITE, "userC", myID);

        assertThrows(DataAccessException.class, () -> {
            sqlGame.gameSpotOpen(myID, ChessGame.TeamColor.WHITE);
        });
    }

    @Test
    public void gameExistsPositive() throws DataAccessException {
        int myID = sqlGame.createGame("realGame");
        assertTrue(sqlGame.gameExists(myID));
    }

    @Test
    public void gameExistsNegative() {
        assertFalse(sqlGame.gameExists(1234));
    }

    @Test
    public void clearTest() throws DataAccessException {
        sqlGame.createGame("Game1");
        sqlGame.createGame("Game2");
        sqlGame.clear();

        assertFalse(sqlGame.gameExists(1));
        assertEquals(0, sqlGame.listGames().size());
    }

}