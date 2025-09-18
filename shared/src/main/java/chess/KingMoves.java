package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    public static Collection<ChessMove> pieceMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int d = -1; d <= 1; d += 2) {
            int dRow = row + d;
            int dCol = col + d;
            ChessPosition upDown = new ChessPosition(dRow, col);
            ChessPosition leftRight = new ChessPosition (row, dCol);
            ChessPosition diagonal1 = new ChessPosition(row+1, dCol);
            ChessPosition diagonal2 = new ChessPosition(row-1, dCol);

            if (isOnBoard(upDown)) {
                ChessPiece target = board.getPiece(upDown);
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, upDown, null));
                }
            }
            if (isOnBoard(leftRight)) {
                ChessPiece target = board.getPiece(leftRight);
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, leftRight, null));
                }
            }
            if (isOnBoard(diagonal1)) {
                ChessPiece target = board.getPiece(diagonal1);
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, diagonal1, null));
                }
            }
            if (isOnBoard(diagonal2)) {
                ChessPiece target = board.getPiece(diagonal2);
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, diagonal2, null));
                }
            }
        }

        return moves;
    }

    private static boolean isOnBoard(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }

}
