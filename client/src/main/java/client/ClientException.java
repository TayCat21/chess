package client;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Indicates there was an error using the client (Responses)
 */
public class ClientException extends Exception {
  public enum Code {
    ServerError,
    ClientError,
  }

  final private Code code;
  private boolean status;

  public ClientException(Code code, String message, Boolean status) {
    super(message);
    this.code = code;
    this.status = status;
  }

  public static ClientException fromJson(String json) {
    var map = new Gson().fromJson(json, HashMap.class);
    boolean status = (boolean) map.get("success");
    String message = map.get("message").toString();
    return new ClientException(Code.ServerError, message, status);
  }

  public static Code fromHttpStatusCode(int httpStatusCode) {
    return switch (httpStatusCode) {
      case 500 -> Code.ServerError;
      case 400 -> Code.ClientError;
      default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
    };
  }