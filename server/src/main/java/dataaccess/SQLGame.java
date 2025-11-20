package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Gamedata;
import service.ListGamesItem;

import java.sql.*;
import java.util.List;

public class SQLGame implements GameDataAccess {
    private int idCounter = 1;

    public SQLGame() {
        try {
            DatabaseManager.setupDatabase(gameStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Gamedata getGame(int gameID) {

        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        int gameID = idCounter;
        idCounter++;
        ChessGame newChessGame = new ChessGame();
        Gamedata newGame = new Gamedata(gameID, null, null, gameName, newChessGame);

        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName," +
                " chessGame) VALUES (?, ?, ?, ?, ?)";
        DatabaseManager.updateDatabase(statement, newGame.gameID(), newGame.whiteUsername(), newGame.blackUsername(),
                newGame.gameName(), newGame.game());
        return gameID;
    }

    @Override
    public List<ListGamesItem> listGames() throws DataAccessException {
        var result = new GamesList();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: %s", e);
        }
        return result;
    }

    private ListGamesItem readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");

        ListGamesItem game = new Gson().fromJson(game.class);
        return game;
    }

    @Override
    public void updateGame(ChessGame.TeamColor color, String username, int gameID) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        DatabaseManager.updateDatabase(statement);
    }

    private final String[] gameStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `chessGame` TEXT,
              PRIMARY KEY (`gameID`)
            )
            """
    };

}
