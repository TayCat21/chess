package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public static Collection<ChessMove> pieceMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        Collection<ChessPosition> possibleMoves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        possibleMoves.add(new ChessPosition(row+1, col+2));
        possibleMoves.add(new ChessPosition(row+1, col-2));
        possibleMoves.add(new ChessPosition(row-1, col+2));
        possibleMoves.add(new ChessPosition(row-1, col-2));
        possibleMoves.add(new ChessPosition(row+2, col+1));
        possibleMoves.add(new ChessPosition(row+2, col-1));
        possibleMoves.add(new ChessPosition(row-2, col+1));
        possibleMoves.add(new ChessPosition(row-2, col-1));

        for (var move : possibleMoves) {
            if (isOnBoard(move)) {
                ChessPiece target = board.getPiece(move);
                if (target == null || target.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, move, null));
                }
            }
        }

        return moves;
    }

    private static boolean isOnBoard(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }



}
