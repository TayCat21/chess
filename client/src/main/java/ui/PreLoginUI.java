package ui;

import client.*;

import java.util.Scanner;
import  static ui.EscapeSequences.*;

public class PreLoginUI {

    ServerFacade server;
    PostLoginUI postLoginUI;

    public PreLoginUI(ServerFacade server) {
        this.server = server;
        postLoginUI = new PostLoginUI(server);
    }

    public void run() {
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        System.out.println("♞ " + SET_TEXT_COLOR_YELLOW + "Welcome to your Chess Hub!" + RESET_TEXT_COLOR +
                "♟ Type" + SET_TEXT_COLOR_BLUE + " 'help' " + RESET_TEXT_COLOR + "for options.");

        boolean signedIn = false;
        Scanner scanner = new Scanner(System.in);

        while (!signedIn) {
            System.out.print(SET_TEXT_COLOR_WHITE + "\n[LOGGED_OUT] >>> " + SET_TEXT_COLOR_LIGHT_GREY);
            String[] userInput = scanner.nextLine().split(" ");

            switch (userInput[0].toLowerCase()) {
                case "help":
                    printHelp("menu");
                    break;
                case "register":
                    if (userInput.length != 4) {
                        System.out.println("Input a Username, Password, and Email with the initial statement:");
                        printHelp("register");
                        break;
                    }

                    try {
                        server.register(userInput[1], userInput[2], userInput[3]);
                        System.out.println("registration successful");
                        signedIn = true;
                        break;
                    } catch(ClientException e) {
                        System.out.println("registration failed: " + e.getMessage());
                        break;
                    }
                case "login":
                    if (userInput.length != 3) {
                        System.out.println("Input your Username and Password in " +
                                "the initial statement:");
                        printHelp("login");
                    }
                    else {
                        try {
                            server.login(userInput[1], userInput[2]);
                            System.out.println("Login successful");
                            signedIn = true;
                        } catch (ClientException e) {
                            System.out.println("Login failed: " + e.getMessage());
                        }
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

        postLoginUI.run();

    }

    private void printHelp(String output) {
        String registerPrint = (SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>");
        String loginPrint = (SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>");

        switch (output) {
            case "register":
                System.out.println(registerPrint);
                break;
            case "login":
                System.out.println(loginPrint);
                break;
            case "menu":
                System.out.println(registerPrint + SET_TEXT_COLOR_LIGHT_GREY + " - to create a new account");
                System.out.println(loginPrint + SET_TEXT_COLOR_LIGHT_GREY + " - to access a current account");
                System.out.println(SET_TEXT_COLOR_BLUE + "quit" +
                        SET_TEXT_COLOR_LIGHT_GREY + " - to close the program");
                System.out.println(SET_TEXT_COLOR_BLUE + "help" +
                        SET_TEXT_COLOR_LIGHT_GREY + " - to display possible commands");
                break;
        }
    }

}
