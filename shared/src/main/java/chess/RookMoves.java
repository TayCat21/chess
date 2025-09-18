package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves {
    public static Collection<ChessMove> pieceMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean rowUp = false;
        boolean rowDown = false;
        boolean colL = false;
        boolean colR = false;

        for (int d = 1; d <= 7; d += 1) {
            ChessPosition up = new ChessPosition(row + d, col);
            if (!rowUp && isOnBoard(up)) {
                ChessPiece target = board.getPiece(up);
                if (target != null) {
                    rowUp = true;
                }
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, up, null));
                }
            }

            ChessPosition down = new ChessPosition(row - d, col);
            if (!rowDown && isOnBoard(down)) {
                ChessPiece target = board.getPiece(down);
                if (target != null) {
                    rowDown = true;
                }
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, down, null));
                }
            }

            ChessPosition right = new ChessPosition(row, col + d);
            if (!colR && isOnBoard(right)) {
                ChessPiece target = board.getPiece(right);
                if (target != null) {
                    colR = true;
                }
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, right, null));
                }
            }

            ChessPosition left = new ChessPosition(row, col - d);
            if (!colL && isOnBoard(left)) {
                ChessPiece target = board.getPiece(left);
                if (target != null) {
                    colL = true;
                }
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, left, null));
                }
            }

        }


        return moves;
    }

    private static boolean isOnBoard(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }

}
