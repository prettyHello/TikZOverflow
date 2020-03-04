package exceptions;

public class FatalException extends RuntimeException {
  public FatalException() {
    super();
  }

  public FatalException(String message) {
    super(message);
  }
}
