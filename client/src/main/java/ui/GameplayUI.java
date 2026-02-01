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
    ChessGame.TeamColor color;

    public GameplayUI(ServerFacade server, ChessGame currentGame, int gameID, ChessGame.TeamColor color) {
        this.server = server;
        this.game = currentGame;
        this.gameID = gameID;
        this.color = color;
    }

    public void run(Boolean observer) {
        boolean playing = true;
        System.out.println(RESET_TEXT_COLOR + RESET_BG_COLOR);

        while (playing && !observer) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(SET_TEXT_COLOR_WHITE + "\n[GAMEPLAY] >>> " + SET_TEXT_COLOR_LIGHT_GREY);
            String[] userInput = scanner.nextLine().split(" ");

            switch (userInput[0].toLowerCase()) {
                case "help":
                    printHelp();
                    break;
                case "redraw":
                    PrintGameBoard.printBoard(color);
                    break;
                case "move":
                    break;
                case "resign":
                    System.out.println("\nAre you sure you want to forfeit the game? (yes/no)");
                    String[] response = scanner.nextLine().split(" ");
                    if(response[0].equalsIgnoreCase("yes")) {
                        try {
                            server.resign(gameID, color);
                        } catch (Exception e) {
                            System.out.println("Error: Forfiet Failed");
                        }
                    } else {
                        System.out.println("Forfiet Cancelled");
                    }
                    break;
                case "highlight":
                    break;
                case "leave":
                    playing = false;
                    System.out.println("\nLeaving Game");
                    try {
                        server.leaveGame(gameID);
                    } catch (Exception e) {
                        System.out.println("Failed to leave the Game");
                    }
                    break;
                default:
                    System.out.println("Unknown Command -- Please try again");
                    printHelp();
                    break;
            }
        }
        while (playing) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(SET_TEXT_COLOR_WHITE + "\n[OBSERVER] >>> " + SET_TEXT_COLOR_LIGHT_GREY);
            String[] userInput = scanner.nextLine().split(" ");

            if (userInput[0].toLowerCase().equals("leave")) {
                playing = false;
                System.out.println("\nLeaving Game");
                try {
                    server.leaveGame(gameID);
                } catch (Exception e) {
                    System.out.println("Failed to leave the Game");
                }
            } else {
                System.out.println("If you wish to leave game type " + SET_TEXT_COLOR_BLUE + "leave");
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
