package websocket.messages;

public class NotificationMsg extends ServerMessage {
    String message;

    public NotificationMsg(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
