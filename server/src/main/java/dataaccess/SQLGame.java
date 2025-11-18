package dataaccess;

import chess.ChessGame;
import model.Gamedata;
import service.ListGamesItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SQLGame implements GameDataAccess {

    public SQLGame() {
        setupDatabase();
    }

    @Override
    public Gamedata getGame(int gameID) {

        return null;
    }

    @Override
    public int createGame(String gameName) {

        return 0;
    }

    @Override
    public List<ListGamesItem> listGames() {
        List gamesList = List.of();
        return gamesList;
    }

    @Override
    public void updateGame(ChessGame.TeamColor color, String username, int gameID) throws DataAccessException {

    }

    @Override
    public void clear() {

    }

    private final String[] gameStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `chessGame` TEXT,
              PRIMARY KEY (`gameID`)
            )
            """
    };

    private void setupDatabase () {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : gameStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
