package dataaccess;

import model.Userdata;

public interface UserDataAccess {

    Userdata getUsername() throws DataAccessException;
}
