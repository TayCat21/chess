package ui;

import chess.ChessGame;
import client.ClientException;
import client.ListGamesItem;
import client.ServerFacade;
import static ui.EscapeSequences.*;
import java.util.Scanner;

public class GameplayUI {
    ServerFacade server;
    ListGamesItem currentGame;

    public GameplayUI(ServerFacade server, ListGamesItem currentGame) {
        this.server = server;
        this.currentGame = currentGame;
    }

    public void run() {
        boolean playing = true;
        System.out.println(RESET_TEXT_COLOR + RESET_BG_COLOR);

        while (playing) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(SET_TEXT_COLOR_WHITE + "\n[GAMEPLAY] >>> " + SET_TEXT_COLOR_LIGHT_GREY);
            String[] userInput = scanner.nextLine().split(" ");

            switch (userInput[0].toLowerCase()) {
                case "help":
                    printHelp();
                    break;
                case "redraw chess board":
                    break;
                case "leave":
                    playing = false;
                    System.out.println("\nLeaving Game");
                    break;
                case "make move":
                    break;
                case "resign":
                    break;
                case "highlight legal moves":
                    break;
                default:
                    System.out.println("Unknown Command -- Please try again");
                    printHelp();
                    break;
            }
        }
    }

    private void printHelp() {
        System.out.println(SET_TEXT_COLOR_BLUE + "make move" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to play a chess piece");
        System.out.println(SET_TEXT_COLOR_BLUE + "highlight legal moves" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to view possible moves for a piece");
        System.out.println(SET_TEXT_COLOR_BLUE + "redraw chess board" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to display current board state");
        System.out.println(SET_TEXT_COLOR_BLUE + "resign" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to forfeit the game");
        System.out.println(SET_TEXT_COLOR_BLUE + "leave" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to close the game");
        System.out.println(SET_TEXT_COLOR_BLUE + "help" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to display possible commands");
    }

}
