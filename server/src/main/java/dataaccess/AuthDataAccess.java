package dataaccess;
import model.Authdata;

public interface AuthDataAccess {

    Authdata getAuth(String authToken) throws DataAccessException;

    void makeAuth(String authToken, String username) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void clear();
}