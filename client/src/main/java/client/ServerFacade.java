package client;

import chess.ChessGame;
import com.google.gson.Gson;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ChessGame register(String username, String password, String email) throws ClientException {
        var request = buildRequest("POST", "/user", username, password, email);
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }
//
//    public RegisterResult login(registerRequest request) {
//    }
//
//    public RegisterResult logout(registerRequest request) {
//    }
//
//    public RegisterResult listGames(registerRequest request) {
//    }
//
//    public RegisterResult createGame(registerRequest request) {
//    }
//
//    public RegisterResult joinGame(registerRequest request) {
//    }

    private HttpRequest buildRequest(String method, String path, Object... body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ClientException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ClientException(ClientException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ClientException {
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
