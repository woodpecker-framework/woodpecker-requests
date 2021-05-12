package me.gv7.woodpecker.requests;

/**
 * Default setting value for request client
 */
public class DefaultSettings {
    /**
     * Default user agent
     */
    public static final String USER_AGENT = "Requests 5.0.3, Java " + System.getProperty("java.version");

    /**
     * Default connect timeout for http connection
     */
    public static final int CONNECT_TIMEOUT = 3000;
    /**
     * Default socks timeout for http connection
     */
    public static final int SOCKS_TIMEOUT = 5000;
}
