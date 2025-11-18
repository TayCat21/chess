package dataaccess;

import model.Userdata;
import java.util.HashSet;

public class MemoryUserDataAccess implements UserDataAccess {

    private final HashSet<Userdata> userBank;

    public MemoryUserDataAccess() {
        userBank = new HashSet<>();
    }

    @Override
    public Userdata getUser(String username) {
        for (Userdata user : userBank) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void addUser(String username, String password, String email) {
        Userdata newUser = new Userdata(username, password, email);
        userBank.add(newUser);
    }

    @Override
    public void clear() {
        userBank.clear();
    }
}
