package be.ac.ulb.infof307.g09.utilities.exceptions;

/**
 * RuntimeException used in case of a business error.
 */
public class BizzException extends RuntimeException {
    public BizzException() {
        super();
    }

    public BizzException(String message) {
        super(message);
    }
}
