package ui;

import chess.ChessGame;
import static ui.EscapeSequences.*;

public class PrintGameBoard {


    public PrintGameBoard() {}


    public static void printInitialBoard(ChessGame.TeamColor color) {
        System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR);
        String space = "   ";
        String ln = (RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + space);

        if (color == ChessGame.TeamColor.WHITE) {
            System.out.print(" a  b  c  d  e  f  g  h  " + space + ln);

            int sideNum = 8;
            String backRow = "RNBQKBRN";
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printBackRow(backRow, "blue");
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            sideNum--;
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printPawns("blue");
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            for (int i = 0; i < 4; i++) {
                sideNum--;
                System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
                printBlankRows(sideNum);
                System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);
            }

            sideNum--;
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printPawns("red");
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

            sideNum--;
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " ");
            printBackRow(backRow, "red");
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + " " + sideNum + " " + ln);

        }
        else if (color == ChessGame.TeamColor.BLACK) {

        }
        else {
            System.out.println("Initial Board Called without defined color");
        }
    }

    static void printPawns(String Color) {
        String pSpace = " P ";

        if (Color.equals("blue")) {
            System.out.print(SET_TEXT_COLOR_BLUE);
        } else {
            System.out.print(SET_TEXT_COLOR_RED);
        }

        for (int j = 0; j < 4; j++) {
            System.out.print(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK + pSpace
                    + SET_BG_COLOR_LIGHT_GREY + pSpace);
        }
    }

    static void printBackRow(String backRow, String Color) {
        int piecePos = 0;
        if (Color.equals("blue")) {
            System.out.print(SET_TEXT_COLOR_BLUE);
        } else {
            System.out.print(SET_TEXT_COLOR_RED);
        }

        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + " " + backRow.charAt(piecePos)
                    + " " + SET_BG_COLOR_BLACK + " " + backRow.charAt(piecePos+1) + " ");
            piecePos = piecePos + 2;
        }
    }

    static void printBlankRows(int sideNum) {
        String space = "   ";

        for (int j = 0; j < 4; j++) {
            if (sideNum % 2 == 0) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY + space + SET_BG_COLOR_BLACK + space);
            } else {
                System.out.print(SET_BG_COLOR_BLACK + space + SET_BG_COLOR_LIGHT_GREY + space);
            }
        }
    }

}
