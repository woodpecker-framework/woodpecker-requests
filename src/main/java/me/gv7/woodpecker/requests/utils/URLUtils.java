package me.gv7.woodpecker.requests.utils;

import me.gv7.woodpecker.requests.Parameter;
import me.gv7.woodpecker.requests.exception.RequestsException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Util methods for encode / decode uri.
 */
public class URLUtils {

    /**
     * Encode key-value form parameter
     */
    public static String encodeForm(Parameter<String> query, Charset charset) {
        try {
            return URLEncoder.encode(query.name(), charset.name()) + "=" + URLEncoder.encode(query.value(),
                    charset.name());
        } catch (UnsupportedEncodingException e) {
            // should not happen
            throw new RequestsException(e);
        }
    }

    /**
     * Encode multi form parameters
     */
    public static String encodeForms(Collection<? extends Parameter<String>> queries, Charset charset) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Parameter<String> query : queries) {
                sb.append(URLEncoder.encode(query.name(), charset.name()));
                sb.append('=');
                sb.append(URLEncoder.encode(query.value(), charset.name()));
                sb.append('&');
            }
        } catch (UnsupportedEncodingException e) {
            // should not happen
            throw new RequestsException(e);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Decode key-value query parameter
     */
    public static Parameter<String> decodeForm(String s, Charset charset) {
        int idx = s.indexOf("=");
        try {
            if (idx < 0) {
                return Parameter.of("", URLDecoder.decode(s, charset.name()));
            }
            return Parameter.of(URLDecoder.decode(s.substring(0, idx), charset.name()),
                    URLDecoder.decode(s.substring(idx + 1), charset.name()));
        } catch (UnsupportedEncodingException e) {
            // should not happen
            throw new RequestsException(e);
        }
    }

    /**
     * Parse query params
     */
    public static List<Parameter<String>> decodeForms(String queryStr, Charset charset) {
        String[] queries = queryStr.split("&");
        List<Parameter<String>> list = new ArrayList<>(queries.length);
        for (String query : queries) {
            list.add(decodeForm(query, charset));
        }

        return list;
    }

    public static List<Parameter<String>> toStringParameters(Collection<? extends Map.Entry<String, ?>> params) {
        List<Parameter<String>> parameters = new ArrayList<>(params.size());
        for (Map.Entry<String, ?> entry : params) {
            parameters.add(Parameter.of(entry.getKey(), String.valueOf(entry.getValue())));
        }
        return parameters;
    }

    public static URL joinUrl(URL url, Collection<? extends Parameter<String>> params, Charset charset) {
        if (params.isEmpty()) {
            return url;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(url.getProtocol()).append(':');
        if (url.getAuthority() != null && !url.getAuthority().isEmpty()) {
            sb.append("//").append(url.getAuthority());
        }
        if (url.getPath() != null) {
            sb.append(url.getPath());
        }

        String query = url.getQuery();
        String newQuery = encodeForms(params, charset);
        if (query == null || query.isEmpty()) {
            sb.append('?').append(newQuery);
        } else {
            sb.append('?').append(query).append('&').append(newQuery);
        }

        if (url.getRef() != null) {
            sb.append('#').append(url.getRef());
        }

        URL fullURL;
        try {
            fullURL = new URL(sb.toString());
        } catch (MalformedURLException e) {
            throw new RequestsException(e);

        }
        return fullURL;
    }
}
