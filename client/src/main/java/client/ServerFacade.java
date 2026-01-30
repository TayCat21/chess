package client;

import chess.ChessGame;
import com.google.gson.Gson;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ServerFacade {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private String serverUrl;
    private String authToken;
    private List<ListGamesItem> updatedGames;

    public ServerFacade(String url) {
        this.serverUrl = url;
        this.updatedGames = new ArrayList<>();
    }

    protected String getUserAuth() {
        return authToken;
    }

    protected void setUserAuth(String token) {
        this.authToken = token;
    }

    protected void setUpdatedGames(List<ListGamesItem> updatedGames) {
        this.updatedGames = updatedGames;
    }

    public List<ListGamesItem> getUpdatedGames() {
        try {
            listGames();
        } catch (ClientException e) {
            throw new RuntimeException(e);
            // This may break a test -- Look into if problems
        }
        return updatedGames;
    }

    public int getListSize() {
        if (updatedGames.isEmpty()) {
            return 0;
        }
        return updatedGames.size();
    }

    public ChessGame register(String username, String password, String email) throws ClientException {
        var body = Map.of("username", username, "password", password, "email", email);
        var request = buildRequest("POST", "/user", body, null);
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    public ChessGame login(String username, String password) throws ClientException {
        var body = Map.of("username", username, "password", password);
        var request = buildRequest("POST", "/session", body, null);
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    public ChessGame logout() throws ClientException {
        var request = buildRequest("DELETE", "/session", null, getUserAuth());
        var response = sendRequest(request);
        setUserAuth(null);
        return handleResponse(response, ChessGame.class);
    }

    public ChessGame listGames() throws ClientException {
        var request = buildRequest("GET", "/game", null, getUserAuth());
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    public ChessGame createGame(String name) throws ClientException {
        var body = Map.of("gameName", name);
        var request = buildRequest("POST", "/game", body, getUserAuth());
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    public ChessGame joinGame(int gameID, String playerColor) throws ClientException {
        var body = Map.of("gameID", gameID, "playerColor", playerColor);
        var request = buildRequest("PUT", "/game", body, getUserAuth());
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    public void clear() throws ClientException {
        var request = buildRequest("DELETE", "/db", null, null);
        var response = sendRequest(request);
        handleResponse(response, ChessGame.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String head) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (head != null) {
            request.header("authorization", head);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ClientException {
        try {
            return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ClientException(ClientException.Code.ServerError, ex.getMessage(), false);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ClientException {
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

        if (response.body() != null && response.body().contains("games")) {
            GameResponse listGame = new Gson().fromJson(response.body(), GameResponse.class);
            setUpdatedGames(listGame.games());
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
