package me.gv7.woodpecker.requests.json;

/**
 * @author Liu Dong
 */
public class JsonProcessorNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5416319717741876095L;

    public JsonProcessorNotFoundException() {
    }

    public JsonProcessorNotFoundException(String message) {
        super(message);
    }

    public JsonProcessorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonProcessorNotFoundException(Throwable cause) {
        super(cause);
    }

    public JsonProcessorNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
