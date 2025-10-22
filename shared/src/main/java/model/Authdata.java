package model;

public record Authdata(String authToken, String username) {

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
    
}
