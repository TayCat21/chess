import chess.*;
import client.ServerFacade;
import ui.PreLoginUI;

public class Main {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);

        String serverUrl = "http://localhost:8080";
        ServerFacade server = new ServerFacade(serverUrl);

        PreLoginUI preLoginUI = new PreLoginUI(server);
        preLoginUI.run();
        System.out.println("Goodbye");
    }
}