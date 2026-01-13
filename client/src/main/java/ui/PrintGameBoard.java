package ui;

import chess.ChessGame;
import static ui.EscapeSequences.*;

public class PrintGameBoard {


    public PrintGameBoard() {

    }

    public static void printInitialBoard(ChessGame.TeamColor color) {
        String space = "   ";
        String pSpace = " P ";
        String backRow = "RNBQKBRN";
        System.out.println(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + space);

        if (color == ChessGame.TeamColor.WHITE) {
            System.out.println(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + space);
            System.out.println(" a  b  c  d  e  f  g  h  " + space);
            int sideNum = 8;
            String sideSpace = (" " + sideNum + " ");

            System.out.println(sideSpace + SET_BG_COLOR_LIGHT_GREY);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.println(SET_BG_COLOR_LIGHT_GREY + space + SET_BG_COLOR_BLACK + space);
                }

                for (int j = 0; j < 4; j++) {
                    System.out.println(SET_BG_COLOR_BLACK + space + SET_BG_COLOR_LIGHT_GREY + space);
                }
            }
        }
        else if (color == ChessGame.TeamColor.BLACK) {

        }
        else {
            System.out.println("Initial Board Called without defined color");
        }
    }

}
