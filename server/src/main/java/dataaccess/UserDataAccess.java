package dataaccess;
import model.Userdata;

public interface UserDataAccess {

    default Userdata getUser(String username) throws DataAccessException {
        return null;
    }

    void addUser(Userdata u) throws DataAccessException;

}
