package me.gv7.woodpecker.requests;

import static java.util.Objects.requireNonNull;

/**
 * Http header
 */
public class Header extends Parameter<String> {
    private static final long serialVersionUID = 4314480501179865952L;

    /**
     * Constructor.
     *
     * use {@link #of(String, String)} instead
     */
    public Header(String key, String value) {
        super(key, value);
    }

    /**
     * Create new header
     *
     * use {@link #of(String, Object)} instead.
     */
    public Header(String name, Object value) {
        this(name, requireNonNull(value).toString());
    }

    /**
     * Create new header.
     * @param name header name
     * @param value header value
     * @return header
     */
    public static Header of(String name, String value) {
        return new Header(requireNonNull(name), requireNonNull(value));
    }

    /**
     * Create new header.
     * @param name header name
     * @param value header value
     * @return header
     */
    public static Header of(String name, Object value) {
        return new Header(requireNonNull(name), String.valueOf(requireNonNull(value)));
    }
}
