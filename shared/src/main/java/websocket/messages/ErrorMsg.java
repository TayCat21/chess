package websocket.messages;

public class ErrorMsg extends ServerMessage {
    String eMessage;

    public ErrorMsg(String eMessage) {
        super(ServerMessageType.ERROR);
        this.eMessage = eMessage;
    }

    public String getMessage() {
        return eMessage;
    }
}
