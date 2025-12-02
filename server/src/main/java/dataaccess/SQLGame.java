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
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    var whiteUsername = rs.getString("whiteUsername");
                    var blackUsername = rs.getString("blackUsername");
                    var gameName = rs.getString("gameName");
                    var chessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
                    return new Gamedata(gameID, whiteUsername, blackUsername, gameName, chessGame);
                }
            }
        } catch (SQLException | DataAccessException e) {
            return null;
        }
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

        return new ListGamesItem(gameID, whiteUsername, blackUsername, gameName);
    }

    @Override
    public void updateGame(ChessGame.TeamColor color, String username, int gameID) throws DataAccessException {
        if (gameSpotOpen(gameID, color)) {
            String statement;
            switch (color) {
                case ChessGame.TeamColor.WHITE -> {
                    statement = "UPDATE game SET whiteUsername = ? WHERE gameID = ?;";
                }
                case ChessGame.TeamColor.BLACK -> {
                    statement = "UPDATE game SET blackUsername = ? WHERE gameID = ?;";
                }
                default -> {
                    throw new DataAccessException("color wasn't selected");
                }
            }
            DatabaseManager.updateDatabase(statement, username, gameID);
        }
        else {
            throw new DataAccessException("bad request");
        }
    }

    public boolean gameSpotOpen(int gameID, ChessGame.TeamColor color) {
        if (gameExists(gameID)) {
            try (Connection conn = DatabaseManager.getConnection()) {
                String statement;
                switch (color) {
                    case ChessGame.TeamColor.WHITE -> {
                        statement = "SELECT whiteUsername FROM game WHERE gameID=?";
                    }
                    case ChessGame.TeamColor.BLACK -> {
                        statement = "SELECT blackUsername FROM game WHERE gameID=?";
                    }
                    default -> {
                        throw new DataAccessException("color wasn't selected");
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(statement)) {
                    ps.setInt(1, gameID);
                    try (ResultSet rs = ps.executeQuery()) {
                        return rs.toString().isEmpty();
                    }
                }
            } catch (SQLException | DataAccessException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean gameExists(int gameID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID FROM game WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException | DataAccessException e) {
            return false;
        }
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
