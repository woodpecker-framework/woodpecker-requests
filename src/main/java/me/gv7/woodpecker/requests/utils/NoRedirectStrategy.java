package me.gv7.woodpecker.requests.utils;

import org.apache.hc.client5.http.protocol.RedirectStrategy;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.net.URI;

public class NoRedirectStrategy implements RedirectStrategy {
    public static final NoRedirectStrategy INSTANCE = new NoRedirectStrategy();

    @Override
    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException {
        return false;
    }

    @Override
    public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException {
        return null;
    }
}
