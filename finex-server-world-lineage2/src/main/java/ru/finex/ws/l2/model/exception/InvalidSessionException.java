package ru.finex.ws.l2.model.exception;

/**
 * @author m0nster.mind
 */
public class InvalidSessionException extends RuntimeException {

    public InvalidSessionException() {
        super();
    }

    public InvalidSessionException(String message) {
        super(message);
    }

    public InvalidSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSessionException(Throwable cause) {
        super(cause);
    }

}
