package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    public static Collection<ChessMove> pieceMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean upR = false;
        boolean downR = false;
        boolean upL = false;
        boolean downL = false;

        for (int d = 1; d <= 7; d += 1) {
            ChessPosition dUpR = new ChessPosition(row + d, col + d);
            if (!upR && isOnBoard(dUpR)) {
                ChessPiece target = board.getPiece(dUpR);
                if (target != null) {
                    upR = true;
                }
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, dUpR, null));
                }
            }

            ChessPosition dUpL = new ChessPosition(row + d, col - d);
            if (!upL && isOnBoard(dUpL)) {
                ChessPiece target = board.getPiece(dUpL);
                if (target != null) {
                    upL = true;
                }
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, dUpL, null));
                }
            }

            ChessPosition dDownR = new ChessPosition(row - d, col + d);
            if (!downR && isOnBoard(dDownR)) {
                ChessPiece target = board.getPiece(dDownR);
                if (target != null) {
                    downR = true;
                }
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, dDownR, null));
                }
            }

            ChessPosition dDownL = new ChessPosition(row - d, col - d);
            if (!downL && isOnBoard(dDownL)) {
                ChessPiece target = board.getPiece(dDownL);
                if (target != null) {
                    downL = true;
                }
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, dDownL, null));
                }
            }
        }

        return moves;
    }

    private static boolean isOnBoard(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }
}
