package be.ac.ulb.infof307.g09.utilities.exceptions;

/**
 * RuntimeException used in case of an unexpected error.
 */
public class FatalException extends RuntimeException {
    public FatalException() {
        super();
    }

    public FatalException(String message) {
        super(message);
    }
}
