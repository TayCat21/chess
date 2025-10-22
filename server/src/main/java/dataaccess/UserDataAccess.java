package dataaccess;

public interface UserDataAccess {

    void getUser(String username);

    void addUser(UserData u) throws DataAccessException;

}
