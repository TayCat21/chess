package dataaccess;

import model.Authdata;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuth implements AuthDataAccess {

    public SQLAuth() {
        setupDatabase();
    }

    @Override
    public Authdata getAuth(String authToken) {

        return null;
    }

    @Override
    public void makeAuth(String authToken, String username) {

    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
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
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
