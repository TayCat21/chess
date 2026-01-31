package client;

import jakarta.websocket.*;

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
        //Server Messages
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    // This method must be overridden, but we don't have to do anything with it
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
