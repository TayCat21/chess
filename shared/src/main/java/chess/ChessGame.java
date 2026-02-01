package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turnColor;
    private ChessBoard board;
    private boolean gameOver;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece myPiece = board.getPiece(startPosition);
        if (myPiece == null) {
            return null;
        }

        Collection<ChessMove> possibleMoves = myPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();

        for (ChessMove move : possibleMoves) {
            ChessBoard fakeBoard = board.copy();
            ChessPosition endPosition = move.getEndPosition();

            board.addPiece(endPosition, myPiece);
            board.addPiece(startPosition, null);

            if (!isInCheck(myPiece.getTeamColor())) {
                validMoves.add(move);
            }

            this.board = fakeBoard;
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition myPosition = move.getStartPosition();
        ChessPiece myPiece = board.getPiece(myPosition);

        if (myPiece == null) {
            throw new InvalidMoveException("No piece found at this position");
        }
        if (myPiece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Piece can't move out of turn");
        }

        Collection<ChessMove> possibleMoves = validMoves(myPosition);
        if (possibleMoves == null) {
            throw new InvalidMoveException("No valid moves can be made");
        }

        if (possibleMoves.contains(move)) {
            if (move.getPromotionPiece() != null) {
                myPiece = new ChessPiece(myPiece.getTeamColor(), move.getPromotionPiece());
            }
            board.addPiece(move.getEndPosition(), myPiece);
            board.addPiece(myPosition, null);

            if (getTeamTurn() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            }
            else if (getTeamTurn() == TeamColor.BLACK) {
                setTeamTurn(TeamColor.WHITE);
            }
        }
        else {
            throw new InvalidMoveException("Move not found in valid moves");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        kingSearch:
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition testPosition = new ChessPosition(r, c);
                ChessPiece testPiece = board.getPiece(testPosition);

                if (testPiece != null) {
                    if (testPiece.getTeamColor() == teamColor && testPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        kingPosition = testPosition;
                        break kingSearch;
                    }
                }
            }
        }

        if (kingPosition == null) {
            return false;
        }
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition testPos = new ChessPosition(r, c);
                ChessPiece testPiece = board.getPiece(testPos);

                if (isOpponentPiece(testPiece, teamColor) && canAttackKing(board, testPiece, testPos, kingPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isOpponentPiece(ChessPiece piece, ChessGame.TeamColor teamColor) {
        return piece != null && piece.getTeamColor() != teamColor;
    }

    private boolean canAttackKing(ChessBoard board, ChessPiece piece, ChessPosition testPos, ChessPosition kingPos) {
        for (ChessMove opponentMove : piece.pieceMoves(board, testPos)) {
            ChessPosition endPosition = opponentMove.getEndPosition();
            if (endPosition.equals(kingPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && !containsAnyValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && !containsAnyValidMoves(teamColor);
    }

    public boolean setGameOver(boolean gameOver) {
        return this.gameOver = gameOver;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean containsAnyValidMoves(TeamColor teamColor) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition testPosition = new ChessPosition(r, c);
                ChessPiece testPiece = board.getPiece(testPosition);

                if (testPiece != null && testPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> possibleMoves = validMoves(testPosition);
                    if (!possibleMoves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turnColor == chessGame.turnColor && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnColor, board);
    }
}
