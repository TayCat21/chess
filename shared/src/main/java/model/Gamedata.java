package model;

import chess.ChessGame;

public record Gamedata(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }


}
