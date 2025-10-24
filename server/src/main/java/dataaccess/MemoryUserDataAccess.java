package dataaccess;

import model.Userdata;
import java.util.HashSet;

public class MemoryUserDataAccess implements UserDataAccess {

    private HashSet<Userdata> userBank;

    public MemoryUserDataAccess() {
        userBank = HashSet.newHashSet(16);
    }

    @Override
    public Userdata getUser(String username) throws DataAccessException {
        for (Userdata user : userBank) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("Couldn't find User: " + username);
    };

    @Override
    public void addUser(Userdata newUser) throws DataAccessException {

    };
}
