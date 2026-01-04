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
        System.out.println("♞ Welcome to your Chess Hub!♟ Type 'help' for options.");

        boolean signedIn = false;
        Scanner scanner = new Scanner(System.in);

        while (!signedIn) {
            System.out.print("\n[LOGGED_OUT] >>> " + SET_TEXT_COLOR_GREEN);
            String[] userInput = scanner.nextLine().split(" ");

            switch (userInput[0].toLowerCase()) {
                case "help":
                    printHelp("menu");
                    break;
                case "register":
                    if (userInput.length != 4) {
                        System.out.println(SET_BG_COLOR_DARK_GREY + "");
                    }
                case "quit":
                    return;
            }
        }

    }

    private void printHelp(String output) {
        String registerPrint = (SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to create a new account");
        String loginPrint = (SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" +
                SET_TEXT_COLOR_LIGHT_GREY + " - to access a current account");

        switch (output) {
            case "register":
                System.out.println(registerPrint);
                break;
            case "login":
                System.out.println(loginPrint);
                break;
            case "menu":
                System.out.println(registerPrint);
                System.out.println(loginPrint);
                System.out.println(SET_TEXT_COLOR_BLUE + "quit" +
                        SET_TEXT_COLOR_LIGHT_GREY + " - to close the program");
                System.out.println(SET_TEXT_COLOR_BLUE + "help" +
                        SET_TEXT_COLOR_LIGHT_GREY + " - to display possible commands");
                break;
        }
    }

}
