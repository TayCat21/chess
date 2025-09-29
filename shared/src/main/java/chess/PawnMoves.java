package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {

    public static Collection<ChessMove> pieceMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (color == ChessGame.TeamColor.WHITE) ? 8 : 1;

        // forward 1
        ChessPosition oneForward = new ChessPosition(row + direction, col);
        if (isOnBoard(oneForward) && board.getPiece(oneForward) == null) {
            if (oneForward.getRow() == promotionRow) {
                addPromotions(moves, myPosition, oneForward);
            } else {
                moves.add(new ChessMove(myPosition, oneForward, null));
            }

        // forward 2
            ChessPosition twoForward = new ChessPosition(row + 2 * direction, col);
            if (row == startRow && board.getPiece(twoForward) == null && board.getPiece(oneForward) == null) {
                moves.add(new ChessMove(myPosition, twoForward, null));
            }
        }

        // diagonal
        for (int dc = -1; dc <= 1; dc += 2) {
            int captureCol = col + dc;
            ChessPosition capturePos = new ChessPosition(row + direction, captureCol);

            if (isOnBoard(capturePos)) {
                ChessPiece target = board.getPiece(capturePos);
                if (target != null && target.getTeamColor() != color) {
                    if (capturePos.getRow() == promotionRow) {
                        addPromotions(moves, myPosition, capturePos);
                    } else {
                        moves.add(new ChessMove(myPosition, capturePos, null));
                    }
                }
            }
        }

        return moves;
    }

    private static boolean isOnBoard(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }

    private static void addPromotions(Collection<ChessMove> moves, ChessPosition start, ChessPosition end) {
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
    }
}
