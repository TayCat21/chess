import chess.*;
import client.ServerFacade;
import ui.PreLoginUI;

public class Main {
    public static void main(String[] args) {

        System.out.println("♕ 240 Chess Client");
        String serverUrl = "http://localhost:8080";
        ServerFacade server = new ServerFacade(serverUrl);

        PreLoginUI preLoginUI = new PreLoginUI(server);
        preLoginUI.run();
        System.out.println("Goodbye");
    }
}