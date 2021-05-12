package me.gv7.woodpecker.requests.exception;

/**
 * Thrown when status code is not 2xx
 */
public class IllegalStatusException extends RequestsException {
    private static final long serialVersionUID = 7652853590053144388L;

    public IllegalStatusException(int statusCode) {
        super("Illegal http status code: " + statusCode);
    }
}
