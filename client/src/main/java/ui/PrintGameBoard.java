package ui;

import chess.ChessGame;
import static ui.EscapeSequences.*;

public class PrintGameBoard {
    ChessGame game;

    public PrintGameBoard(ChessGame game) {
        this.game = game;
    }

    public void uploadGame(ChessGame game) {
        this.game = game;
    }


    public static void printInitialBoard(ChessGame.TeamColor color) {
        System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR + RESET_TEXT_UNDERLINE);
        String ln = (RESET_BG_COLOR + "\n");

        if (color == ChessGame.TeamColor.WHITE) {
            printBoarder(true);

            int sideNum = 8;
            String backRow = "RNBQKBRN";
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printBackRow(false, backRow, true);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            sideNum--;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printPawns(true, false);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            for (int i = 0; i < 4; i++) {
                sideNum--;
                System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
                printBlankRows(sideNum, true);
                System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);
            }

            sideNum--;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printPawns(false, true);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            sideNum--;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printBackRow(true, backRow, false);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            printBoarder(true);
        }
        else if (color == ChessGame.TeamColor.BLACK) {

            printBoarder(false);

            int sideNum = 1;
            String backRow = "RNBKQBRN";
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printBackRow(false, backRow, false);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            sideNum++;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printPawns(false, false);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            for (int i = 0; i < 4; i++) {
                sideNum++;
                System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
                printBlankRows(sideNum, false);
                System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);
            }

            sideNum++;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printPawns(true, true);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            sideNum++;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printBackRow(true, backRow, true);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            printBoarder(false);

        }
        else {
            System.out.println("Initial Board Called without defined color");
        }
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

    static void printPawns(Boolean blue, Boolean front) {
        String pSpace = " P ";
        String colorOne;
        String colorTwo;

        if (blue) {
            System.out.print(SET_TEXT_COLOR_BLUE);
        } else {
            System.out.print(SET_TEXT_COLOR_RED);
        }

        if (front) {
            colorOne = SET_BG_COLOR_WHITE;
            colorTwo = SET_BG_COLOR_BLACK;
        }
        else {
            colorOne = SET_BG_COLOR_BLACK;
            colorTwo = SET_BG_COLOR_WHITE;
        }
        for (int j = 0; j < 4; j++) {
            System.out.print(colorOne + pSpace
                    + colorTwo + pSpace);
        }
    }

    static void printBackRow(Boolean front, String backRow, Boolean blue) {
        int piecePos = 0;
        String colorOne;
        String colorTwo;

        if (blue) {
            System.out.print(SET_TEXT_COLOR_BLUE);
        } else {
            System.out.print(SET_TEXT_COLOR_RED);
        }

        if (!front) {
            colorOne = SET_BG_COLOR_WHITE;
            colorTwo = SET_BG_COLOR_BLACK;
        }
        else {
            colorOne = SET_BG_COLOR_BLACK;
            colorTwo = SET_BG_COLOR_WHITE;
        }
        for (int i = 0; i < 4; i++) {
            System.out.print(colorOne + " " + backRow.charAt(piecePos)
                    + " " + colorTwo + " " + backRow.charAt(piecePos + 1) + " ");
            piecePos = piecePos + 2;
        }

    }

    static void printBlankRows(int sideNum, boolean white) {
        String space = "   ";
        String colorOne;
        String colorTwo;

        if (white) {
            colorOne = SET_BG_COLOR_WHITE;
            colorTwo = SET_BG_COLOR_BLACK;
        }
        else {
            colorOne = SET_BG_COLOR_BLACK;
            colorTwo = SET_BG_COLOR_WHITE;
        }

        for (int j = 0; j < 4; j++) {
            if (sideNum % 2 == 0) {
                System.out.print(colorOne + space + colorTwo + space);
            } else {
                System.out.print(colorTwo + space + colorOne + space);
            }
        }
    }

}
