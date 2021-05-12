package me.gv7.woodpecker.requests.executor;

import me.gv7.woodpecker.requests.Cookie;
import me.gv7.woodpecker.requests.utils.Cookies;

import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * CookieJar that store cookie in memory, maintaining cookies following RFC 6265
 */
class DefaultCookieJar implements CookieJar, Serializable {

    private static final long serialVersionUID = 8372575235144209124L;
    private Map<CookieKey, Cookie> cookieMap = new HashMap<>();

    @Override
    public synchronized void storeCookies(Collection<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            CookieKey key = new CookieKey(cookie.domain(), cookie.path(), cookie.name());
            cookieMap.put(key, cookie);
        }
        removeExpiredCookies();
    }

    private void removeExpiredCookies() {
        long now = System.currentTimeMillis();
        cookieMap.entrySet().removeIf(entry -> entry.getValue().expired(now));
    }

    @Override
    public synchronized List<Cookie> getCookies(URL url) {
        long now = System.currentTimeMillis();
        List<Cookie> matched = new ArrayList<>();
        for (Cookie cookie : cookieMap.values()) {
            if (!Cookies.match(cookie, url.getProtocol(), url.getHost().toLowerCase(), url.getPath())) {
                continue;
            }
            if (cookie.expired(now)) {
                continue;
            }
            matched.add(cookie);
        }
        // we did not sort using create time here
        matched.sort((cookie1, cookie2) -> cookie2.path().length() - cookie1.path().length());
        return matched;
    }

    @Override
    public synchronized List<Cookie> getCookies() {
        return new ArrayList<>(cookieMap.values());
    }

    private static class CookieKey {
        private final String domain;
        private final String path;
        private final String name;

        public CookieKey(String domain, String path, String name) {
            this.domain = domain;
            this.path = path;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CookieKey cookieKey = (CookieKey) o;

            if (!domain.equals(cookieKey.domain)) return false;
            if (!path.equals(cookieKey.path)) return false;
            return name.equals(cookieKey.name);
        }

        @Override
        public int hashCode() {
            int result = domain.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }
}
