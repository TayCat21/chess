package client;

/**
 * Indicates there was an error using the client (Responses)
 */
public class ClientException extends RuntimeException {
    public ClientException(String message) {
      super(message);
    }
  public ClientException(String message, Throwable ex) {
    super(message, ex);
  }
}