package me.gv7.woodpecker.requests.executor;

import me.gv7.woodpecker.requests.*;
import me.gv7.woodpecker.requests.utils.*;
import net.dongliu.commons.io.InputStreams;
import me.gv7.woodpecker.requests.body.RequestBody;
import me.gv7.woodpecker.requests.exception.RequestsException;
import me.gv7.woodpecker.requests.exception.TooManyRedirectsException;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.util.Timeout;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static me.gv7.woodpecker.requests.HttpHeaders.*;
import static me.gv7.woodpecker.requests.StatusCodes.*;

/**
 * Execute http request with url connection
 *
 * @author Liu Dong
 */
class URLConnectionExecutor implements HttpExecutor {

    static {
        // we can modify Host, and other restricted headers
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        System.setProperty("http.keepAlive", "true");
        // default is 5
        System.setProperty("http.maxConnections", "100");
    }

    @Override
    public RawResponse proceed(Request request) {
        RawResponse response = doRequest(request);

        int statusCode = response.statusCode();
        if (!request.followRedirect() || !isRedirect(statusCode)) {
            return response;
        }

        // handle redirect
        response.discardBody();
        int redirectTimes = 0;
        final int maxRedirectTimes = request.maxRedirectCount();
        URL redirectUrl = request.url();
        while (redirectTimes++ < maxRedirectTimes) {
            String location = response.getHeader(NAME_LOCATION);
            if (location == null) {
                throw new RequestsException("Redirect location not found");
            }
            try {
                redirectUrl = new URL(redirectUrl, location);
            } catch (MalformedURLException e) {
                throw new RequestsException("Resolve redirect url error, location: " + location, e);
            }
            String method = request.method();
            RequestBody<?> body = request.body();
            if (statusCode == MOVED_PERMANENTLY || statusCode == FOUND || statusCode == SEE_OTHER) {
                // 301/302 change method to get, due to historical reason.
                method = Methods.GET;
                body = null;
            }

            RequestBuilder builder = request.toBuilder().method(method).url(redirectUrl)
                    .followRedirect(false).body(body);
            response = builder.send();
            if (!isRedirect(response.statusCode())) {
                return response;
            }
            response.discardBody();
        }
        throw new TooManyRedirectsException(maxRedirectTimes);
    }

    private static boolean isRedirect(int status) {
        return status == MULTIPLE_CHOICES || status == MOVED_PERMANENTLY || status == FOUND || status == SEE_OTHER
                || status == TEMPORARY_REDIRECT || status == PERMANENT_REDIRECT;
    }

    private RawResponse doRequest(Request request) {
        Charset charset = request.charset();
        URL url = URLUtils.joinUrl(request.url(), URLUtils.toStringParameters(request.params()), charset);
        @Nullable RequestBody<?> body = request.body();
        CookieJar cookieJar;
        if (request.sessionContext() != null) {
            cookieJar = request.sessionContext().cookieJar();
        } else {
            cookieJar = NopCookieJar.instance;
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(request.connectTimeout())) // 连接超时
                .setResponseTimeout(Timeout.ofMilliseconds(request.socksTimeout())); // 响应超时

        HttpClientBuilder httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(requestConfigBuilder.build());
        if (!request.verify()) {
            try {
                httpClientBuilder.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create().setTlsSocketStrategy(
                        new DefaultClientTlsStrategy(SSLContextFactories.getTrustAllSSLContext(), NopHostnameVerifier.getInstance())
                ).build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (request.keyStore() != null) {
            httpClientBuilder.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create().setTlsSocketStrategy(
                    new DefaultClientTlsStrategy(SSLContextFactories.getCustomTrustSSLContext(request.keyStore()))
            ).build());
        }
        httpClientBuilder.setRedirectStrategy(NoRedirectStrategy.INSTANCE);
        if (request.proxy() != null) {
            Proxy proxy = request.proxy();
            if (proxy.type() != Proxy.Type.DIRECT) {
                httpClientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(getHostFromProxy(proxy)));
            }
        }
        try {
            HttpUriRequestBase req = new HttpUriRequestBase(request.method(), url.toURI());

            if (!request.userAgent().isEmpty()) {
                req.setHeader(NAME_HOST, request.url().getAuthority());
            }

            if (!request.userAgent().isEmpty()) {
                req.setHeader(NAME_USER_AGENT, request.userAgent());
            }

            if (request.acceptCompress()) {
                req.setHeader(NAME_ACCEPT_ENCODING, "gzip, deflate");
            }

            if (request.basicAuth() != null) {
                req.setHeader(NAME_AUTHORIZATION, request.basicAuth().encode());
            }
            if (body != null) {
                String contentType = body.contentType();
                if (contentType != null) {
                    if (body.includeCharset()) {
                        contentType += "; charset=" + request.charset().name().toLowerCase();
                    }
                    req.setHeader(NAME_CONTENT_TYPE, contentType);
                }
            }
            // set cookies
            Collection<Cookie> sessionCookies = cookieJar.getCookies(url);
            if (!request.cookies().isEmpty() || !sessionCookies.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, ?> entry : request.cookies()) {
                    sb.append(entry.getKey()).append("=").append(String.valueOf(entry.getValue())).append("; ");
                }
                for (Cookie cookie : sessionCookies) {
                    sb.append(cookie.name()).append("=").append(cookie.value()).append("; ");
                }
                if (sb.length() > 2) {
                    sb.setLength(sb.length() - 2);
                    String cookieStr = sb.toString();
                    req.setHeader(NAME_COOKIE, cookieStr);
                }
            }

            // set user custom headers
            for (Map.Entry<String, ?> header : request.headers()) {
                req.setHeader(header.getKey(), String.valueOf(header.getValue()));
            }

            // disable keep alive
            if (!request.keepAlive()) {
                req.setHeader("Connection", "close");
            }

            if (body != null) {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    body.writeBody(out, charset);
                    req.setEntity(new ByteArrayEntity(out.toByteArray(), null));
                }
            }
            org.apache.hc.core5.http.Header ua = req.getLastHeader(NAME_USER_AGENT);
            if (ua != null && ua.getValue() != null && !"".equalsIgnoreCase(ua.getValue())) {
                httpClientBuilder.setUserAgent(ua.getValue());
            }
            CloseableHttpClient httpClient = httpClientBuilder.build();
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                    security.checkConnect(request.url().getHost(),
                            request.url().getPort());
            }
            return readResponse(httpClient, httpClient.execute(req), url, cookieJar, request.method());
        } catch (URISyntaxException | IOException e) {
            throw new RequestsException(e);
        }

    }

    private RawResponse readResponse(CloseableHttpClient httpClient, CloseableHttpResponse response, URL url, CookieJar cookieJar, String method) throws IOException {
        int status = response.getCode();
        String host = url.getHost().toLowerCase();

        final String statusLine = null;
        // headers and cookies
        List<Header> headerList = new ArrayList<>();
        List<Cookie> cookies = new ArrayList<>();
        for (org.apache.hc.core5.http.Header header : response.getHeaders()) {
            String key = header.getName();
            String value = header.getValue();
            headerList.add(new Header(key, value));
            if (key.equalsIgnoreCase(NAME_SET_COOKIE)) {
                Cookie c = Cookies.parseCookie(value, host, Cookies.calculatePath(url.getPath()));
                if (c != null) {
                    cookies.add(c);
                }
            }
        }
        Headers headers = new Headers(headerList);
        InputStream input = null;
        if (response.getEntity() != null) {
            input = response.getEntity().getContent();
        }
        if (input == null) {
            input = InputStreams.empty();
        }
        cookieJar.storeCookies(cookies);
        return new RawResponse(method, url.toExternalForm(), status, "", cookies, headers, input, httpClient);
    }


    private void sendBody(RequestBody body, HttpURLConnection conn, Charset requestCharset) {
        try (OutputStream os = conn.getOutputStream()) {
            body.writeBody(os, requestCharset);
        } catch (IOException e) {
            throw new RequestsException(e);
        }
    }


    private HttpHost getHostFromProxy(Proxy proxy) {
        InetSocketAddress address = (InetSocketAddress) proxy.address();
        return new HttpHost(proxy.type().name().toLowerCase(), address.getHostName(), address.getPort());
    }
}
