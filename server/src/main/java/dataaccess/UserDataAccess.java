package dataaccess;
import model.Userdata;

public interface UserDataAccess {

    Userdata getUser(String username) throws DataAccessException;

    void addUser(String username, String password, String email) throws DataAccessException;

    boolean matchingPass(String inputPass, String hashedPass);

    void clear() throws DataAccessException;
}
