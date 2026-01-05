package ui;

import client.ClientException;
import client.ServerFacade;
import model.Gamedata;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PostLoginUI {
    ServerFacade server;
    List<Gamedata> myGames;

    public PostLoginUI(ServerFacade server) {
        this.server = server;
        myGames = new ArrayList<>();
    }

    public void run() {
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        boolean signedIn = true;
        boolean inGame = false;

        while (signedIn && !inGame) {
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

                    server.createGame(userInput[1]);
                    System.out.printf("Created Game: %s%n", userInput[1]);
                    break;
                case "join":
                    //handle join
                    break;
                case "observe":
                    //handle observe
                    return;
                case "list":
                    //print Games
                    break;
                case "logout":
                    server.logout();
                    signedIn = false;
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
