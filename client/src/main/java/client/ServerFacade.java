package client;

import chess.ChessGame;
import com.google.gson.Gson;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


public class ServerFacade {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private String serverUrl;
    private String authToken;

    public ServerFacade(String url) throws Exception {
        this.serverUrl = url;
    }

    protected String getUserAuth() {
        return authToken;
    }

    protected void setUserAuth(String token) {
        this.authToken = token;
    }

    public ChessGame register(String username, String password, String email) throws ClientException {
        var body = Map.of("username", username, "password", password, "email", email);
        var request = buildRequest("POST", "/user", body);
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    public ChessGame login(String username, String password) throws ClientException {
        var body = Map.of("username", username, "password", password);
        var request = buildRequest("POST", "/session", body);
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    public ChessGame logout() throws ClientException {
        var request = buildRequest("DELETE", "/session", null);
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

//    public RegisterResult listGames(registerRequest request) {
//    }
//
    public void createGame(String name) {
    }
//
//    public RegisterResult joinGame(registerRequest request) {
//    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        System.out.println("---Building Request---");
        var request = HttpRequest.newBuilder().uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        System.out.println("---Building Request Body---");
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ClientException {
        System.out.println("---Sending Request---");
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ClientException(ClientException.Code.ServerError, ex.getMessage(), false);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ClientException {
        System.out.println("---Handling Response---");
        System.out.println(response.body());
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ClientException.fromJson(body);
            }

            throw new ClientException(ClientException.fromHttpStatusCode(status), "other failure: " + status, false);
        }

        if (response.body() != null && response.body().contains("authToken")) {
            AuthResponse auth = new Gson().fromJson(response.body(), AuthResponse.class);
            String authToken = auth.authToken();
            setUserAuth(authToken);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
