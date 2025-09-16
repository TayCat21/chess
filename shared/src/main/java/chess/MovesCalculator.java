package chess;

import java.util.Collection;

public class MovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessPiece piece, ChessBoard board, ChessPosition position) {
        switch (piece.getPieceType()) {
            case KING:
                return KingMoves.pieceMoves(piece.getTeamColor(), board, position);
            case QUEEN:
                return QueenMoves.pieceMoves(piece.getTeamColor(), board, position);
            case BISHOP:
                return BishopMoves.pieceMoves(piece.getTeamColor(), board, position);
            case KNIGHT:
                return KnightMoves.pieceMoves(piece.getTeamColor(), board, position);
            case ROOK:
                return RookMoves.pieceMoves(piece.getTeamColor(), board, position);
            case PAWN:
                return PawnMoves.pieceMoves(piece.getTeamColor(), board, position);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + piece.getPieceType());
        }
    }
}
