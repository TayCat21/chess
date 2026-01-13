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

    public ServerFacade(String url) throws Exception {
        this.serverUrl = url;
    }

    public ChessGame register(String username, String password, String email) throws ClientException {
        System.out.println(username + " " + password + " " + email);
        var body = Map.of("username", username, "password", password, "email", email);
        var request = buildRequest("POST", "/user", body);
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    public void login(String username, String password) {
    }

    public void logout() {
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
            throw new ClientException(ClientException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ClientException {
        System.out.println("---Handling Request---");
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ClientException.fromJson(body);
            }

            throw new ClientException(ClientException.fromHttpStatusCode(status), "other failure: " + status);
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
