package dataaccess;

import model.Authdata;
import java.util.HashSet;

public class MemoryAuthDataAccess implements AuthDataAccess {

    private final HashSet<Authdata> tokenBank;

    public MemoryAuthDataAccess() {
        tokenBank = new HashSet<>();
    }

    @Override
    public Authdata getAuth(String authToken) {
        for (Authdata token : tokenBank) {
            if (token.authToken().equals(authToken)) {
                return token;
            }
        }
        return null;
    }

    @Override
    public void makeAuth(String authToken, String username) {
        Authdata newAuth = new Authdata(authToken, username);
        tokenBank.add(newAuth);
    }

    @Override
    public void deleteAuth(String authToken) {
        tokenBank.removeIf(token -> token.authToken().equals(authToken));
    }

    @Override
    public void clear() {
        tokenBank.clear();
    }

}
