package dataaccess;

import model.Authdata;
import com.google.gson.Gson;
import java.sql.*;

public class SQLAuth implements AuthDataAccess {

    public SQLAuth() {
        setupDatabase();
    }

    @Override
    public Authdata getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, authToken FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void makeAuth(String authToken, String username) throws DataAccessException {
        Authdata token = new Authdata(username, authToken);
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        DatabaseManager.updateDatabase(statement, token.username(), token.authToken());
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() {

    }

    private final String[] authStatements = {
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
            for (String statement : authStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Authdata readAuth(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var authToken = rs.getString("authToken");
        Authdata token = new Gson().fromJson(username, authToken);
        return token;
    }
}
