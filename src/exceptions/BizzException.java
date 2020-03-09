package exceptions;

/**
 * RuntimeException used in case of a buisness error.
 */
public class BizzException extends RuntimeException {
  public BizzException() {
    super();
  }

  public BizzException(String message) {
    super(message);
  }
}
