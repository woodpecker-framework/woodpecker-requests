package me.gv7.woodpecker.requests.exception;

/**
 * Thrown when something wrong occurred when load certificate, construct trust manager, etc.
 */
public class TrustManagerLoadFailedException extends RequestsException {

    public TrustManagerLoadFailedException(Exception e) {
        super(e);
    }

    public TrustManagerLoadFailedException(String message) {
        super(message);
    }
}
