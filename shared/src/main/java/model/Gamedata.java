package model;

import chess.ChessGame;

import java.util.Objects;

public class Gamedata {
    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;
    private final ChessGame game;

    Gamedata(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gamedata gamedata = (Gamedata) o;
        return gameID == gamedata.gameID && Objects.equals(whiteUsername, gamedata.whiteUsername) && Objects.equals(blackUsername, gamedata.blackUsername) && Objects.equals(gameName, gamedata.gameName) && Objects.equals(game, gamedata.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
