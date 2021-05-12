package me.gv7.woodpecker.requests;


import java.io.Serializable;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class Cookie extends Parameter<String> implements Map.Entry<String, String>, Serializable {
    private static final long serialVersionUID = -287880603936079757L;
    /**
     * The cookie domain set by attribute or from url
     */
    private final String domain;
    /**
     * The cookie path set by attribute or from url
     */
    private final String path;
    /**
     * The cookie expire timestamp, zero means no expiry is set
     */
    private final long expiry;
    /**
     * If secure attribute is set
     */
    private final boolean secure;

    /**
     * If true, the cookie did not set domain attribute
     */
    private final boolean hostOnly;

    public Cookie(String domain, String path, String name, String value, long expiry, boolean secure,
            boolean hostOnly) {
        super(name, value);
        this.domain = requireNonNull(domain);
        this.path = requireNonNull(path);
        this.expiry = expiry;
        this.secure = secure;
        this.hostOnly = hostOnly;
    }

    /**
     * If cookie is expired
     */
    public boolean expired(long now) {
        return expiry != 0 && expiry < now;
    }

    /**
     * @deprecated use {@link #domain()}
     */
    @Deprecated
    public String getDomain() {
        return domain;
    }

    /**
     * @deprecated use {@link #secure()}
     */
    @Deprecated
    public boolean isSecure() {
        return secure;
    }

    /**
     * @deprecated use {@link #expiry()}
     */
    @Deprecated
    public long getExpiry() {
        return expiry;
    }

    /**
     * @deprecated use {@link #path()}
     */
    @Deprecated
    public String getPath() {
        return path;
    }

    /**
     * @deprecated use {@link #hostOnly()}
     */
    @Deprecated
    public boolean isHostOnly() {
        return hostOnly;
    }

    public String domain() {
        return domain;
    }

    public boolean secure() {
        return secure;
    }

    public long expiry() {
        return expiry;
    }

    public String path() {
        return path;
    }

    public boolean hostOnly() {
        return hostOnly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cookie cookie = (Cookie) o;

        if (expiry != cookie.expiry) return false;
        if (secure != cookie.secure) return false;
        if (hostOnly != cookie.hostOnly) return false;
        if (!domain.equals(cookie.domain)) return false;
        if (!path.equals(cookie.path)) return false;
        if (!name.equals(cookie.name)) return false;
        return value.equals(cookie.value);
    }

    @Override
    public int hashCode() {
        int result = domain.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + (int) (expiry ^ (expiry >>> 32));
        result = 31 * result + (secure ? 1 : 0);
        result = 31 * result + (hostOnly ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "domain='" + domain + '\'' +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", expiry=" + expiry +
                ", secure=" + secure +
                ", hostOnly=" + hostOnly +
                '}';
    }
}
