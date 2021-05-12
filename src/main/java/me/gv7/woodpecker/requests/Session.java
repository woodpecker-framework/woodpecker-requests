package me.gv7.woodpecker.requests;

import me.gv7.woodpecker.requests.executor.SessionContext;

import java.net.URL;
import java.util.List;

/**
 * Http request share cookies etc.
 * This class is Thread-Safe.
 */
public class Session {

    private final SessionContext context;

    Session(SessionContext context) {
        this.context = context;
    }

    public RequestBuilder get(String url) {
        return newRequest(Methods.GET, url);
    }

    public RequestBuilder post(String url) {
        return newRequest(Methods.POST, url);
    }

    public RequestBuilder put(String url) {
        return newRequest(Methods.PUT, url);
    }

    public RequestBuilder head(String url) {
        return newRequest(Methods.HEAD, url);
    }

    public RequestBuilder delete(String url) {
        return newRequest(Methods.DELETE, url);
    }

    public RequestBuilder patch(String url) {
        return newRequest(Methods.PATCH, url);
    }

    public RequestBuilder newRequest(String method, String url) {
        return new RequestBuilder().sessionContext(context).url(url).method(method);
    }

    public RequestBuilder get(URL url) {
        return newRequest(Methods.GET, url);
    }

    public RequestBuilder post(URL url) {
        return newRequest(Methods.POST, url);
    }

    public RequestBuilder put(URL url) {
        return newRequest(Methods.PUT, url);
    }

    public RequestBuilder head(URL url) {
        return newRequest(Methods.HEAD, url);
    }

    public RequestBuilder delete(URL url) {
        return newRequest(Methods.DELETE, url);
    }

    public RequestBuilder patch(URL url) {
        return newRequest(Methods.PATCH, url);
    }


    /**
     * Return all cookies this session current hold.
     */
    public List<Cookie> currentCookies() {
        return context.cookieJar().getCookies();
    }

    /**
     * Create new request with method and url
     */
    public RequestBuilder newRequest(String method, URL url) {
        return new RequestBuilder().sessionContext(context).url(url).method(method);
    }
}
