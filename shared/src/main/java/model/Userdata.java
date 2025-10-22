package model;

import java.util.Objects;

public class Userdata {
    private final String username;
    private final String password;
    private final String email;

    Userdata(String username, String password, String email) {

        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Userdata userdata = (Userdata) o;
        return Objects.equals(username, userdata.username) && Objects.equals(password, userdata.password) && Objects.equals(email, userdata.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }
}
