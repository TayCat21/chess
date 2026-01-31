package ui;

import chess.ChessGame;
import client.ClientException;
import client.ListGamesItem;
import client.ServerFacade;
import static ui.EscapeSequences.*;
import java.util.Scanner;

public class GameplayUI {
    ServerFacade server;
    ChessGame game;
    int gameID;
    public static ChessGame.TeamColor color;

    public GameplayUI(ServerFacade server, ChessGame currentGame) {
        this.server = server;
        this.game = currentGame;
        // get ID & color
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
                case "redraw":
                    break;
                case "move":
                    break;
                case "resign":
                    break;
                case "highlight":
                    break;
                case "leave":
                    playing = false;
                    System.out.println("\nLeaving Game");
//                    server.leave(gameID);
                    break;
                default:
                    System.out.println("Unknown Command -- Please try again");
                    printHelp();
                    break;
            }
        }
    }

    private void printHelp() {
        System.out.println(SET_TEXT_COLOR_BLUE + "move" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to move a chess piece");
        System.out.println(SET_TEXT_COLOR_BLUE + "highlight" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to highlight  legal moves for a piece");
        System.out.println(SET_TEXT_COLOR_BLUE + "redraw" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to display current board state");
        System.out.println(SET_TEXT_COLOR_BLUE + "resign" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to forfeit the game");
        System.out.println(SET_TEXT_COLOR_BLUE + "leave" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to close the game");
        System.out.println(SET_TEXT_COLOR_BLUE + "help" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to display possible commands");
    }

}
