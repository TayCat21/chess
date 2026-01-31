package client;

import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.messages.*;
import static ui.EscapeSequences.*;
import java.io.IOException;
import java.net.URI;

public class WebsocketCommunicator extends Endpoint {

    Session session;

    public WebsocketCommunicator(String serverURL) throws Exception {
        URI uri = new URI("ws:" + serverURL + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler((MessageHandler.Whole<String>) this::handleMessage);
    }

    private void handleMessage(String message) {
        if (message.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
            NotificationMsg notification = new Gson().fromJson(message, NotificationMsg.class);
            printMsg(notification.getMessage());
        }
        else if (message.contains("\"serverMessageType\":\"ERROR\"")) {
            ErrorMsg error = new Gson().fromJson(message, ErrorMsg.class);
            printMsg(error.getMessage());
        }
        else if (message.contains("\"serverMessageType\":\"LOAD_GAME\"")) {
            LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
            printGame(loadGame.getColor());
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void printMsg(String message) {
        System.out.print(SET_TEXT_COLOR_DARK_GREY + SET_TEXT_ITALIC);
        System.out.println(message);
    }

    public void printGame(ChessGame.TeamColor color) {
        //update game
        ui.PrintGameBoard.printBoard(color);
    }

    // This method must be overridden, but we don't have to do anything with it
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
