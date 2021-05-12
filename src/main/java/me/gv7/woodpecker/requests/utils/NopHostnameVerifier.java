package me.gv7.woodpecker.requests.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author Liu Dong
 */
public class NopHostnameVerifier implements HostnameVerifier {

    private static class Holder {
        private static NopHostnameVerifier instance = new NopHostnameVerifier();
    }

    public static HostnameVerifier getInstance() {
        return Holder.instance;
    }

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
