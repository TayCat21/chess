package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
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
    private boolean gameOver;

    public boolean setGameOver(boolean gameOver) {
        return this.gameOver = gameOver;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public GameplayUI(ServerFacade server, ChessGame currentGame, int gameID, ChessGame.TeamColor color) {
        this.server = server;
        this.game = currentGame;
        this.gameID = gameID;
        this.color = color;
    }

    public void waitSec() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.print(SET_TEXT_COLOR_WHITE);
        }
    }

    public void run(Boolean observer) {
        waitSec();
        boolean playing = true;
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);

        while (playing && !observer) {
            System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
            System.out.print(SET_TEXT_COLOR_WHITE + "\n[GAMEPLAY] >>> " + SET_TEXT_COLOR_LIGHT_GREY);
            Scanner scanner = new Scanner(System.in);
            String[] userInput = scanner.nextLine().split(" ");

            switch (userInput[0].toLowerCase()) {
                case "help":
                    printHelp("menu");
                    break;
                case "redraw":
                    PrintGameBoard.printBoard(color, null);
                    break;
                case "move":
                    makeMove(userInput);
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
                    waitSec();
                    break;
                case "highlight":
                    if (userInput.length == 2 && userInput[1].matches("[a-h][1-8]")) {
                        ChessPosition position = new ChessPosition(userInput[1].charAt(1) - '0',
                                userInput[1].charAt(0) - ('a' - 1));
                        PrintGameBoard.printBoard(color, position);
                    }
                    else {
                        System.out.println("Input a piece coordinate with the initial statement: (ex: 'b5')");
                        printHelp("high");
                    }
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
                    printHelp("menu");
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
                    System.out.println("Error: Failed to leave the Game");
                }
            } else {
                System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "If you wish to leave game type " + SET_TEXT_COLOR_BLUE + "leave");
            }
        }
    }

    private void makeMove(String[] userInput) {
        if (userInput.length >= 3 && userInput[1].matches("[a-h][1-8]") &&
                userInput[2].matches("[a-h][1-8]")) {
            ChessPosition fromPos = new ChessPosition(userInput[1].charAt(1) - '0',
                    userInput[1].charAt(0) - ('a' - 1));
            ChessPosition toPos = new ChessPosition(userInput[2].charAt(1) - '0',
                    userInput[2].charAt(0) - ('a' - 1));

            ChessPiece.PieceType promote = null;
            if (userInput.length == 4) {
                promote = getPieceType(userInput[3]);
                if (promote == null) {
                    System.out.println("Please include a proper promotion piece title (ex: rook)");
                    printHelp("move");
                }
            }

            try {
                server.makeMove(gameID, color, new ChessMove(fromPos, toPos, promote));
            } catch (Exception e) {
                System.out.println("Error: Failed to make move: " + e);
            }
        }
        else {
            System.out.println("Input both a to and from coordinate with the initial statement: (ex: 'b5 c4')");
            printHelp("move");
        }
    }

    public ChessPiece.PieceType getPieceType(String name) {
        return switch (name.toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            case "PAWN" -> ChessPiece.PieceType.PAWN;
            default -> null;
        };
    }

    private void printHelp(String output) {
        String movePrint = (SET_TEXT_COLOR_BLUE + "move <from> <to> <promotion_piece>");
        String highPrint = (SET_TEXT_COLOR_BLUE + "highlight <coordinate>");

        switch (output) {
            case "move":
                System.out.println(movePrint);
                break;
            case "high":
                System.out.println(highPrint);
                break;
            case "menu":
                System.out.println(movePrint + SET_TEXT_COLOR_LIGHT_GREY + " - to move a chess piece (Leave " +
                        "promotion_piece empty unless you are promoting a pawn)");
                System.out.println(highPrint + SET_TEXT_COLOR_BLUE + "highlight <coordinate>" +
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

}
