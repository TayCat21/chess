package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class PrintGameBoard {
    static ChessGame game;

    public PrintGameBoard(ChessGame game) {
        this.game = game;
    }

    public void uploadGame(ChessGame game) {
        this.game = game;
    }


    static void printBoarder(Boolean white) {
        String space = "   ";
        String ln = (RESET_BG_COLOR + "\n");
        if (!white) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + space
                    + " h  g  f  e  d  c  b  a " + space + ln);
        }
        else {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + space
                    + " a  b  c  d  e  f  g  h " + space + ln);
        }
    }

    public static void printBoard(ChessGame.TeamColor color, ChessPosition pickedPos) {
        System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR + RESET_TEXT_UNDERLINE);
        var board = game.getBoard();
        String ln = (RESET_BG_COLOR + "\n");
        Collection<ChessMove> posHighlight = (pickedPos != null) ? game.validMoves(pickedPos) : null;
        HashSet<ChessPosition> squareHighlight = HashSet.newHashSet(posHighlight != null ? posHighlight.size():0);
        if (posHighlight != null) {
            for (ChessMove start : posHighlight) {
                squareHighlight.add(start.getEndPosition());
            }
        }

        printBoarder(color == ChessGame.TeamColor.WHITE);

        int colorRow = 0;
        int row = (color == ChessGame.TeamColor.WHITE) ? 8 : 1;
        int plusMinus = (color == ChessGame.TeamColor.WHITE) ? -1 : 1;
        for (int sideNum = 8; sideNum > 0; sideNum--) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (row) + " ");
            String rowColor = (colorRow % 2 == 0) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
            String opColor = (colorRow % 2 == 0) ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE;
            int col = (color == ChessGame.TeamColor.WHITE) ? 1 : 8;
            for (int colNum = 1; colNum <= 8; colNum++) {
                ChessPosition currentSquare = new ChessPosition(row, col);
                String squareColor = (colNum % 2 == 0) ? opColor : rowColor;
                if (currentSquare.equals(pickedPos) || squareHighlight.contains(currentSquare)) {
                    boolean mainPoint = (currentSquare.equals(pickedPos));
                    squareColor = getHighlight(squareColor, mainPoint);
                }
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String printSquare = getSquare(piece, squareColor);
                System.out.print(printSquare);
                col = col - plusMinus;
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (row) + " " + ln);
            row = row + plusMinus;
            colorRow++;
        }
        printBoarder(color == ChessGame.TeamColor.WHITE);
    }

    public static String getSquare(ChessPiece piece, String colorBG) {
        if (piece == null) {
            return (colorBG + "   ");
        }
        String pieceColor = pColorCheck(piece);
        String pieceType = pTypeCheck(piece);
        return (colorBG + pieceColor + pieceType);
    }

    public static String getHighlight(String ogColor, boolean startPos) {
        if (startPos) {
            return SET_BG_COLOR_BLUE;
        }
        return (Objects.equals(ogColor, SET_BG_COLOR_BLACK))?SET_BG_COLOR_DARK_GREEN:SET_BG_COLOR_GREEN;
    }

    public static String pColorCheck(ChessPiece piece) {
        switch (piece.getTeamColor()) {
            case ChessGame.TeamColor.WHITE:
                return SET_TEXT_COLOR_RED;

            case ChessGame.TeamColor.BLACK:
                return SET_TEXT_COLOR_BLUE;
        }
        return null;
    }

    public static String pTypeCheck(ChessPiece piece) {
        switch (piece.getPieceType()) {
            case KING:
                return " K ";
            case QUEEN:
                return " Q ";
            case ROOK:
                return " R ";
            case KNIGHT:
                return " N ";
            case BISHOP:
                return " B ";
            case PAWN:
                return " P ";
        }
        return null;
    }

}
