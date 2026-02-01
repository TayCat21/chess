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
                    handleCreate(userInput[1]);
                    break;
                case "join":
                    if (userInput.length != 3) {
                        System.out.println("Input both a Game ID and Color Selection with the initial statement:");
                        printHelp("join");
                        break;
                    }
                    int gameID = validID(userInput[1]);
                    if (gameID == 0) {
                        printHelp("join");
                        System.out.println(SET_TEXT_COLOR_BLUE + "list" + SET_TEXT_COLOR_LIGHT_GREY);
                        break;
                    }
                    String colorChoice = userInput[2].toUpperCase();
                    ChessGame.TeamColor color = validColor(colorChoice);
                    if (colorChoice == null) {
                        System.out.println("Color Selection not recognized. Select either"
                                + SET_TEXT_COLOR_BLUE + " WHITE" + SET_TEXT_COLOR_LIGHT_GREY + " or"
                                + SET_TEXT_COLOR_BLUE + " BLACK" + SET_TEXT_COLOR_LIGHT_GREY);
                        break;
                    }
                    try {
                        server.joinGame(gameID, colorChoice);
                    } catch (ClientException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    enterGame(gameID, color, false);
                    break;
                case "observe":
                    if (userInput.length != 2) {
                        System.out.println("Input a Game ID with the initial statement:");
                        printHelp("observe");
                        break;
                    }
                    int observeID = validID(userInput[1]);
                    if (observeID == 0) {
                        printHelp("observe");
                        System.out.println(SET_TEXT_COLOR_BLUE + "list" + SET_TEXT_COLOR_LIGHT_GREY);
                        break;
                    }
                    System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "\nYou have entered this game as an observer");
                    enterGame(observeID, ChessGame.TeamColor.WHITE, true);
                    break;
                case "list":
                    doList();
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

    private void enterGame(int gameID, ChessGame.TeamColor color, Boolean observer) {
        ChessGame currentGame = printBoard(color, gameID);
        server.connectWS();
        try {
            if (!observer) {
                server.playGame(gameID, color);
            }
            else {
                server.observeGame(gameID);
            }
        } catch (Exception e) {
            System.out.println("Couldn't connect to game: " + e);
        }
        GameplayUI gameplayUI = new GameplayUI(server, currentGame, gameID, color);
        gameplayUI.run(observer);
    }

    private void doList() {
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
    }

    private ChessGame.TeamColor validColor(String colorChoice) {
        ChessGame.TeamColor color;
        if (colorChoice.equals("WHITE")) {
            color = ChessGame.TeamColor.WHITE;
        }
        else if (colorChoice.equals("BLACK")) {
            color = ChessGame.TeamColor.BLACK;
        }
        else {
            color = null;
        }
        return color;
    }

    private int validID(String gameID) {
        try {
            int myInt = Integer.parseInt(gameID);

            if (server.getListSize() == 0) {
                System.out.println("View Game list to see available games");
                return 0;
            } else if (myInt <= 0 || myInt > server.getListSize()) {
                System.out.println("Please input a valid Game ID from the Game List");
                return 0;
            }

            return myInt;
        } catch (NumberFormatException e) {
            System.out.println("Please input a Game ID Number from the Game List");
        }
        return 0;
    }

    private ChessGame printBoard(ChessGame.TeamColor color, int gameID) {
        List<ListGamesItem> gamesList = server.getUpdatedGames();
        var currentGame = gamesList.get(gameID-1);
        try {
            var game = server.getBoard(currentGame.gameID());

            System.out.print("\n          ");
            System.out.println(SET_TEXT_COLOR_GREEN + SET_TEXT_UNDERLINE + currentGame.gameName());
            new PrintGameBoard(game);
            PrintGameBoard.printBoard(color);
            System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR + RESET_TEXT_UNDERLINE);
                return game;
            } catch (ClientException e) {
                System.out.println("getBoard Error: " + e);
            }
                return null;
        }

    private void handleCreate(String gameName) {
        try {
            server.createGame(gameName);
            System.out.printf("Created Game: %s%n", gameName);
        } catch (ClientException e) {
            System.out.println(e.getMessage());
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
