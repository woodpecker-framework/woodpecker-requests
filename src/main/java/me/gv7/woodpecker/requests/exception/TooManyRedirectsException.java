package me.gv7.woodpecker.requests.exception;

/**
 * Thrown when redirect too many times.
 */
public class TooManyRedirectsException extends RequestsException {
    private static final long serialVersionUID = 6781138499271333117L;

    public TooManyRedirectsException(int count) {
        super("Redirect too many times: " + count);
    }
}
