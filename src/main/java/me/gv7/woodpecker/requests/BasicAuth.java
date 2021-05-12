package me.gv7.woodpecker.requests;


import java.io.Serializable;
import java.util.Base64;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Http Basic Authentication
 *
 * @author Liu Dong
 */
public class BasicAuth implements Serializable {
    private static final long serialVersionUID = 7453526434365174929L;
    private final String user;
    private final String password;

    public BasicAuth(String user, String password) {
        this.user = Objects.requireNonNull(user);
        this.password = Objects.requireNonNull(password);
    }

    /**
     * @deprecated use {@link #user()}
     */
    @Deprecated
    public String getUser() {
        return user;
    }

    /**
     * @deprecated use {@link #password()}
     */
    @Deprecated
    public String getPassword() {
        return password;
    }

    public String user() {
        return user;
    }

    public String password() {
        return password;
    }

    /**
     * Encode to http header
     */
    public String encode() {
        return "Basic " + Base64.getEncoder().encodeToString((user + ":" + password).getBytes(UTF_8));
    }

}
