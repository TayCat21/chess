package ui;

import client.ClientException;
import client.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PostLoginUI {
    public PostLoginUI(ServerFacade server) {}

    public void run() {
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        System.out.println("♞ " + SET_TEXT_COLOR_YELLOW + "Welcome Logged-in User!" + RESET_TEXT_COLOR +
                "♟ Type" + SET_TEXT_COLOR_BLUE + " 'help' " + RESET_TEXT_COLOR + "for options.");

        Scanner scanner = new Scanner(System.in);
        System.out.print(SET_TEXT_COLOR_WHITE + "\n[LOGGED_IN] >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String[] userInput = scanner.nextLine().split(" ");
        System.out.println(userInput);

    }
}
