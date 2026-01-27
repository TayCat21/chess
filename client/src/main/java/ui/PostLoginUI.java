package ui;

import chess.ChessGame;
import client.ClientException;
import client.ListGamesItem;
import client.ServerFacade;

import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PostLoginUI {
    ServerFacade server;

    public PostLoginUI(ServerFacade server) {
        this.server = server;
    }

    public void run() {
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        boolean signedIn = true;

        while (signedIn) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(SET_TEXT_COLOR_WHITE + "\n[LOGGED_IN] >>> " + SET_TEXT_COLOR_LIGHT_GREY);
            String[] userInput = scanner.nextLine().split(" ");

            switch (userInput[0].toLowerCase()) {
                case "help":
                    printHelp("menu");
                    break;
                case "create":
                    if (userInput.length != 2) {
                        System.out.println("Input a single-word Game Title with the initial statement:");
                        printHelp("create");
                        break;
                    }

                    try {
                        server.createGame(userInput[1]);
                        System.out.printf("Created Game: %s%n", userInput[1]);
                    } catch (ClientException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "join":
                    if (userInput.length != 3) {
                        System.out.println("Input both a Game ID and Color Selection with the initial statement:");
                        printHelp("join");
                        break;
                    }
                    try {
                        int myInt = Integer.parseInt(userInput[1]);

                        if (server.getListSize() == 0) {
                            System.out.println("View Game list to see available games");
                            break;
                        } else if(myInt < 0 || myInt > server.getListSize()) {
                            System.out.println("Please input a valid Game ID from the Game List");
                            break;
                        }

                        String colorChoice = userInput[2].toUpperCase();
                        ChessGame.TeamColor color;
                        if (colorChoice.equals("WHITE")) {
                            color = ChessGame.TeamColor.WHITE;
                        }
                        else if (colorChoice.equals("BLACK")) {
                            color = ChessGame.TeamColor.BLACK;
                        }
                        else {
                            System.out.println("Color Selection not recognized. Select either"
                                    + SET_TEXT_COLOR_BLUE + " WHITE" + SET_TEXT_COLOR_LIGHT_GREY + " or"
                                    + SET_TEXT_COLOR_BLUE + " BLACK" + SET_TEXT_COLOR_LIGHT_GREY);
                            break;
                        }

                        try {
                             server.joinGame(myInt, colorChoice);
                        } catch (ClientException e) {
                            System.out.println(e.getMessage());
                            break;
                        }

                        List<ListGamesItem> gamesList = server.getUpdatedGames();
                        var currentGame = gamesList.get(myInt-1);

                        System.out.println(SET_TEXT_COLOR_GREEN + SET_TEXT_UNDERLINE + currentGame.gameName());
                        PrintGameBoard.printInitialBoard(color);

                        GameplayUI gameplayUI = new GameplayUI(server, currentGame);
                        gameplayUI.run();
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Please input a valid Game ID from the Game List");
                        printHelp("join");
                        break;
                    }
                case "observe":
                    //handle observe
                    return;
                case "list":
                    try {
                        server.listGames();
                        List<ListGamesItem> updatedGames = server.getUpdatedGames();
                        for (int i = 1; i <= server.getListSize(); i++){
                            var gameItem = updatedGames.get(i-1);
                            System.out.print(SET_TEXT_COLOR_WHITE + "[" + SET_TEXT_COLOR_GREEN + i + SET_TEXT_COLOR_WHITE + "] ");
                            System.out.print(SET_TEXT_COLOR_GREEN + gameItem.gameName() + SET_TEXT_COLOR_WHITE);
                            System.out.print(" - WHITE: " + SET_TEXT_COLOR_LIGHT_GREY + gameItem.whiteUsername());
                            System.out.println(SET_TEXT_COLOR_WHITE + " BLACK: " + SET_TEXT_COLOR_LIGHT_GREY
                                    + gameItem.blackUsername());
                        }
                    } catch (ClientException e) {
                        System.out.println("Couldn't List Games: " + e.getMessage());
                    }
                    break;
                case "logout":
                    try {
                        server.logout();
                        signedIn = false;
                    } catch (ClientException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "quit":
                    System.out.print(SET_TEXT_COLOR_YELLOW);
                    return;
                default:
                    System.out.println("Unknown Command -- Please try again");
                    printHelp("menu");
                    break;
            }
        }
        if (!signedIn) {
            PreLoginUI preLoginUI  = new PreLoginUI(server);
            preLoginUI.run();
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
