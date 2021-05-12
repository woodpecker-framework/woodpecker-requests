package me.gv7.woodpecker.requests.executor;

import net.dongliu.commons.collection.Lists;
import me.gv7.woodpecker.requests.Cookie;

import java.net.URL;
import java.util.Collection;
import java.util.List;

/**
 * Cookie jar that do nothing. Used for plain request.
 */
class NopCookieJar implements CookieJar {

    static final NopCookieJar instance = new NopCookieJar();

    private NopCookieJar() {
    }

    @Override
    public void storeCookies(Collection<Cookie> cookies) {

    }

    @Override
    public List<Cookie> getCookies(URL url) {
        return Lists.of();
    }

    @Override
    public List<Cookie> getCookies() {
        return Lists.of();
    }
}
