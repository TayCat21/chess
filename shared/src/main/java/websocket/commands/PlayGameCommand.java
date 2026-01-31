package websocket.commands;

import chess.ChessGame;

public class PlayGameCommand extends UserGameCommand {
    int gameID;
    ChessGame.TeamColor playerColor;

    public PlayGameCommand(CommandType connect, String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(connect, authToken, gameID);
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getColor() {
        return playerColor;
    }
}
