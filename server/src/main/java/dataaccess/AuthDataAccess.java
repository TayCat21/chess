package dataaccess;
import model.Authdata;

public interface AuthDataAccess {

    Authdata getAuth(String authToken);

    void makeAuth(String authToken, String username);

    void deleteAuth(String authToken);
}