package dataaccess;

import model.Authdata;
import com.google.gson.Gson;
import java.sql.*;

public class SQLAuth implements AuthDataAccess {

    public SQLAuth() {
        try {
            DatabaseManager.setupDatabase(authStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Authdata getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    rs.getString("authToken");
                    var username = rs.getString("username");
                    return new Authdata(authToken, username);
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void makeAuth(String authToken, String username) throws DataAccessException {
        Authdata token = new Authdata(authToken, username);
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        DatabaseManager.updateDatabase(statement, token.authToken(), token.username());
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        DatabaseManager.updateDatabase(statement, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        DatabaseManager.updateDatabase(statement);
    }

    private final String[] authStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };
}
