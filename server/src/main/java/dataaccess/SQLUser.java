package dataaccess;

import model.Userdata;
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

        return null;
    }

    @Override
    public void addUser(String username, String password, String email) throws DataAccessException {
        Userdata newUser = new Userdata(username, password, email);
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.updateDatabase(statement, newUser.username(), newUser.password(), newUser.email());
    }

    @Override
    public void clear() {

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

}
