package exceptions;

public class BizzException extends RuntimeException {
  public BizzException() {
    super();
  }

  public BizzException(String message) {
    super(message);
  }
}
