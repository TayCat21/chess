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
                    System.out.println("type 'leave' to return to menu");
                    break;
                case "redraw chess board":
                    break;
                case "leave":
                    playing = false;
                    break;
                case "make move":
                    break;
                case "resign":
                    break;
                case "highlight legal moves":
                    break;
                default:
                    System.out.println("Unknown Command -- Please try again");
                    break;
            }
        }
    }

    private void printHelp(String output) {
        String createPrint = (SET_TEXT_COLOR_BLUE + "create <NAME>");
        String joinPrint = (SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]");
        String observePrint = (SET_TEXT_COLOR_BLUE + "observe <ID>");

        switch (output) {
            case "create":
                System.out.println(createPrint);
                break;
            case "join":
                System.out.println(joinPrint);
                break;
            case "observe":
                System.out.println(observePrint);
                break;
            case "menu":
                System.out.println(createPrint + SET_TEXT_COLOR_LIGHT_GREY + " - to create a new game");
                System.out.println(SET_TEXT_COLOR_BLUE + "list" +
                        SET_TEXT_COLOR_LIGHT_GREY + " - to view all games");
                System.out.println(joinPrint + SET_TEXT_COLOR_LIGHT_GREY + " - to join a game");
                System.out.println(observePrint + SET_TEXT_COLOR_LIGHT_GREY + " - to watch a game");
                System.out.println(SET_TEXT_COLOR_BLUE + "logout" +
                        SET_TEXT_COLOR_LIGHT_GREY + " - to sign out of the program");
                System.out.println(SET_TEXT_COLOR_BLUE + "quit" +
                        SET_TEXT_COLOR_LIGHT_GREY + " - to close the program");
                System.out.println(SET_TEXT_COLOR_BLUE + "help" +
                        SET_TEXT_COLOR_LIGHT_GREY + " - to display possible commands");
                break;
        }
    }

}
