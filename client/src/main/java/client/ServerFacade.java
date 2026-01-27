package client;

import chess.ChessGame;
import com.google.gson.Gson;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static ui.EscapeSequences.*;


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
//
//    public RegisterResult joinGame(registerRequest request) {
//    }

    private HttpRequest buildRequest(String method, String path, Object body, String head) {
        System.out.println("---Building Request---");
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
        System.out.println("---Sending Request---");
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ClientException(ClientException.Code.ServerError, ex.getMessage(), false);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ClientException {
        System.out.println("---Handling Response---");
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
            List<ListGamesItem> gamesList = listGame.games();
            for (int i = 1; i <= gamesList.size(); i++){
                var gameItem = gamesList.get(i-1);
                System.out.print(SET_TEXT_COLOR_WHITE + "[" + SET_TEXT_COLOR_GREEN + i + SET_TEXT_COLOR_WHITE + "] ");
                System.out.print(SET_TEXT_COLOR_GREEN + gameItem.gameName() + SET_TEXT_COLOR_WHITE);
                System.out.print(" - WHITE: " + SET_TEXT_COLOR_LIGHT_GREY + gameItem.whiteUsername());
                System.out.println(SET_TEXT_COLOR_WHITE + " BLACK: " + SET_TEXT_COLOR_LIGHT_GREY
                        + gameItem.blackUsername());
            }
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
