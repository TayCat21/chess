package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    ChessMove move;
    ChessGame.TeamColor color;

    public MakeMoveCommand(CommandType connect, String authToken, int gameID, ChessGame.TeamColor color, ChessMove move) {
        super(connect, authToken, gameID);
        this.color = color;
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
