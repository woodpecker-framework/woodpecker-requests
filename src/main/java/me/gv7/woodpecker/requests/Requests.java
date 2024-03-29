package me.gv7.woodpecker.requests;


import me.gv7.woodpecker.requests.executor.RequestExecutorFactory;

import java.net.URL;

/**
 * Http request utils methods.
 *
 * @author Liu Dong
 */
public class Requests {

    /**
     * Start a GET request
     */
    public static RequestBuilder get(URL url) {
        return newRequest(Methods.GET, url);
    }

    /**
     * Start a POST request
     */
    public static RequestBuilder post(URL url) {
        return newRequest(Methods.POST, url);
    }

    /**
     * Start a PUT request
     */
    public static RequestBuilder put(URL url) {
        return newRequest(Methods.PUT, url);
    }

    /**
     * Start a DELETE request
     */
    public static RequestBuilder delete(URL url) {
        return newRequest(Methods.DELETE, url);
    }

    /**
     * Start a HEAD request
     */
    public static RequestBuilder head(URL url) {
        return newRequest(Methods.HEAD, url);
    }

    /**
     * Start a PATCH request
     */
    public static RequestBuilder patch(URL url) {
        return newRequest(Methods.PATCH, url);
    }

    /**
     * Create new request with method and url
     */
    public static RequestBuilder newRequest(String method, URL url) {
        return new RequestBuilder().method(method).url(url);
    }

    public static RequestBuilder get(String url) {
        return newRequest(Methods.GET, url);
    }

    public static RequestBuilder post(String url) {
        return newRequest(Methods.POST, url);
    }

    public static RequestBuilder put(String url) {
        return newRequest(Methods.PUT, url);
    }

    public static RequestBuilder delete(String url) {
        return newRequest(Methods.DELETE, url);
    }

    public static RequestBuilder head(String url) {
        return newRequest(Methods.HEAD, url);
    }

    public static RequestBuilder patch(String url) {
        return newRequest(Methods.PATCH, url);
    }

    public static RequestBuilder method(String method,String url) throws Exception {
        if(method.equals("GET")){
            return newRequest(Methods.GET, url);
        }else if(method.equals("POST")){
            return newRequest(Methods.POST, url);
        }else if(method.equals("PUT")){
            return newRequest(Methods.PUT, url);
        }else if(method.equals("DELETE")){
            return newRequest(Methods.DELETE, url);
        }else if(method.equals("HEAD")){
            return newRequest(Methods.HEAD, url);
        }else if(method.equals("PATCH")){
            return newRequest(Methods.PATCH, url);
        }else{
            throw new Exception(String.format("%s is an unknown method",method));
        }
    }

    /**
     * Create new request with method and url
     */
    public static RequestBuilder newRequest(String method, String url) {
        return new RequestBuilder().method(method).url(url);
    }

    /**
     * Create new session.
     */
    public static Session session() {
        RequestExecutorFactory factory = RequestExecutorFactory.getInstance();
        return new Session(factory.newSessionContext());
    }
}
