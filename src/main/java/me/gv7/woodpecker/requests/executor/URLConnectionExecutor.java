package me.gv7.woodpecker.requests.executor;

import me.gv7.woodpecker.requests.*;
import net.dongliu.commons.io.InputStreams;
import me.gv7.woodpecker.requests.body.RequestBody;
import me.gv7.woodpecker.requests.exception.RequestsException;
import me.gv7.woodpecker.requests.exception.TooManyRedirectsException;
import me.gv7.woodpecker.requests.utils.Cookies;
import me.gv7.woodpecker.requests.utils.NopHostnameVerifier;
import me.gv7.woodpecker.requests.utils.SSLSocketFactories;
import me.gv7.woodpecker.requests.utils.URLUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

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
        /*
         *  2021.11.2 @Ppsoft1991
         *  解决如果CookieHandler存在,cookie会重复的bug
         *  加了层判断，如果存在CookieHandler,则由系统的CookieManager对cookie进行管理
         * */
        if (request.sessionContext() != null && CookieHandler.getDefault()==null) {
            cookieJar = request.sessionContext().cookieJar();
        } else {
            cookieJar = NopCookieJar.instance;
        }

        @Nullable Proxy proxy = request.proxy();
        if (proxy == null) {
            proxy = Proxy.NO_PROXY;
        }
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection(proxy);
        } catch (IOException e) {
            throw new RequestsException(e);
        }

        // disable cache
        conn.setUseCaches(false);

        // deal with https
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            if (!request.verify()) {
                SSLSocketFactory ssf = SSLSocketFactories.getTrustAllSSLSocketFactory();
                httpsConn.setSSLSocketFactory(ssf);
                // do not verify host of certificate
                httpsConn.setHostnameVerifier(NopHostnameVerifier.getInstance());
            } else if (request.keyStore() != null) {
                SSLSocketFactory ssf = SSLSocketFactories.getCustomTrustSSLSocketFactory(request.keyStore());
                httpsConn.setSSLSocketFactory(ssf);
            }
        }

        try {
            conn.setRequestMethod(request.method());
        } catch (ProtocolException e) {
            throw new RequestsException(e);
        }
        conn.setReadTimeout(request.socksTimeout());
        conn.setConnectTimeout(request.connectTimeout());
        // Url connection did not deal with cookie when handle redirect. Disable it and handle it manually
        conn.setInstanceFollowRedirects(false);
        if (body != null) {
            conn.setDoOutput(true);
            String contentType = body.contentType();
            if (contentType != null) {
                if (body.includeCharset()) {
                    contentType += "; charset=" + request.charset().name().toLowerCase();
                }
                conn.setRequestProperty(NAME_CONTENT_TYPE, contentType);
            }
        }

        // headers
        if (!request.userAgent().isEmpty()) {
            conn.setRequestProperty(NAME_USER_AGENT, request.userAgent());
        }
        if (request.acceptCompress()) {
            conn.setRequestProperty(NAME_ACCEPT_ENCODING, "gzip, deflate");
        }

        if (request.basicAuth() != null) {
            conn.setRequestProperty(NAME_AUTHORIZATION, request.basicAuth().encode());
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
                conn.setRequestProperty(NAME_COOKIE, cookieStr);
            }
        }

        // set user custom headers
        for (Map.Entry<String, ?> header : request.headers()) {
            conn.setRequestProperty(header.getKey(), String.valueOf(header.getValue()));
        }

        // disable keep alive
        if (!request.keepAlive()) {
            conn.setRequestProperty("Connection", "close");
        }

        try {
            conn.connect();
        } catch (IOException e) {
            throw new RequestsException(e);
        }

        try {
            // send body
            if (body != null) {
                sendBody(body, conn, charset);
            }
            return getResponse(url, conn, cookieJar, request.method());
        } catch (IOException e) {
            conn.disconnect();
            throw new RequestsException(e);
        } catch (Throwable e) {
            conn.disconnect();
            throw e;
        }
    }

    /**
     * Wrap response, deal with headers and cookies
     */
    private RawResponse getResponse(URL url, HttpURLConnection conn, CookieJar cookieJar, String method)
            throws IOException {
        // read result
        int status = conn.getResponseCode();
        String host = url.getHost().toLowerCase();

        String statusLine = null;
        // headers and cookies
        List<Header> headerList = new ArrayList<>();
        List<Cookie> cookies = new ArrayList<>();
        /*
         *  2021.11.2 @Ppsoft
         *  getHeaderField会走到filter导致HttpOnly Cookie无法获取，所以替换为getHeaderFields来绕过
         * */
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        for (Map.Entry<String, List<String>> next : headerFields.entrySet()) {
            String key = next.getKey();
            List<String> value = next.getValue();
            if (value == null) {
                break;
            }
            //status line
            if (key == null) {
                statusLine = value.get(0);
                continue;
            }
            // cookie
            if (value.size() > 1) {
                for (String cookie : value) {
                    Cookie c = Cookies.parseCookie(cookie, host, Cookies.calculatePath(url.getPath()));
                    if (c != null) {
                        cookies.add(c);
                    }
                }
            } else {
                headerList.add(new Header(key, value.get(0)));
            }
        }
        Headers headers = new Headers(headerList);

        InputStream input;
        try {
            input = conn.getInputStream();
        } catch (IOException e) {
            input = conn.getErrorStream();
        }
        if (input == null) {
            input = InputStreams.empty();
        }

        // update session
        cookieJar.storeCookies(cookies);
        return new RawResponse(method, url.toExternalForm(), status, statusLine == null ? "" : statusLine,
                cookies, headers, input, conn);
    }


    private void sendBody(RequestBody body, HttpURLConnection conn, Charset requestCharset) {
        try (OutputStream os = conn.getOutputStream()) {
            body.writeBody(os, requestCharset);
        } catch (IOException e) {
            throw new RequestsException(e);
        }
    }
}
