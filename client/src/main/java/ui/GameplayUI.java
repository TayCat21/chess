package ui;

import chess.ChessGame;
import client.ClientException;
import client.ServerFacade;
import static ui.EscapeSequences.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GameplayUI {
    ServerFacade server;

    public GameplayUI(ServerFacade server) {
        this.server = server;
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
//                    printHelp("menu");
                    break;

                case "leave":
                    playing = false;
                    break;
                case "quit":
                    System.out.print(SET_TEXT_COLOR_YELLOW);
                    return;
                default:
                    System.out.println("Unknown Command -- Please try again");
//                    printHelp("menu");
                    break;
            }
        }
    }
}
