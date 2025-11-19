package dataaccess;

import model.Userdata;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


public class SQLUser implements UserDataAccess {

    public SQLUser() {
        try {
            DatabaseManager.setupDatabase(userStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Userdata getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    var password = rs.getString("password");
                    var email = rs.getString("email");
                    return new Userdata(username, password, email);
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void addUser(String username, String password, String email) throws DataAccessException {
        Userdata newUser = new Userdata(username, password, email);
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.updateDatabase(statement, newUser.username(),
                hashPassword(newUser.password()), newUser.email());
    }

    @Override
    public boolean matchingPass(String inputPass, String hashedPass) {
        return BCrypt.checkpw(inputPass, hashedPass);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        DatabaseManager.updateDatabase(statement);
    }

    private final String[] userStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

    private String hashPassword(String clearPassword) {
        return BCrypt.hashpw(clearPassword, BCrypt.gensalt());
    }

}
