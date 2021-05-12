package me.gv7.woodpecker.requests.exception;

/**
 * Thrown when request failed.
 *
 * @author Liu Dong
 */
public class RequestsException extends RuntimeException {
    private static final long serialVersionUID = -932950698709129457L;

    public RequestsException(String msg) {
        super(msg);
    }

    public RequestsException(String msg, Exception e) {
        super(msg, e);
    }

    public RequestsException(Exception e) {
        super(e);
    }
}
