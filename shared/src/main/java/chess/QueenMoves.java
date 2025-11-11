package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {
    public static Collection<ChessMove> pieceMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(RookMoves.pieceMoves(color, board, myPosition));

        moves.addAll(BishopMoves.pieceMoves(color, board, myPosition));

        return moves;
    }
}
