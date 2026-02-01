package service;

import chess.ChessGame;

public record RemovePlayerRequest(int gameID, ChessGame.TeamColor playerColor) {
}
