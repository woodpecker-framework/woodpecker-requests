package me.gv7.woodpecker.requests.utils;

import me.gv7.woodpecker.requests.exception.TrustManagerLoadFailedException;
import me.gv7.woodpecker.requests.exception.RequestsException;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Utils method for ssl socket factory
 *
 * @author Liu Dong
 */
public class SSLSocketFactories {

    // To reuse the connection, settings on the underlying socket must use the exact same objects.

    private static final SSLSocketFactory sslSocketFactoryLazy = _getTrustAllSSLSocketFactory();

    public static SSLSocketFactory _getTrustAllSSLSocketFactory() {
        TrustManager trustManager = new TrustAllTrustManager();
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RequestsException(e);
        }

        return sslContext.getSocketFactory();
    }


    public static SSLSocketFactory getTrustAllSSLSocketFactory() {
        return sslSocketFactoryLazy;
    }

    private static final ConcurrentMap<KeyStore, SSLSocketFactory> map = new ConcurrentHashMap<>();

    private static SSLSocketFactory _getCustomSSLSocketFactory(KeyStore keyStore) {
        TrustManager trustManager = new CustomCertTrustManager(keyStore);
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RequestsException(e);
        }

        return sslContext.getSocketFactory();
    }

    public static SSLSocketFactory getCustomTrustSSLSocketFactory(KeyStore keyStore) {
        if (!map.containsKey(keyStore)) {
            map.put(keyStore, _getCustomSSLSocketFactory(keyStore));
        }
        return map.get(keyStore);
    }

    static class TrustAllTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * Trust Manager that trust additional x509 certificates provided by user
     */
    static class CustomCertTrustManager implements X509TrustManager {

        private final KeyStore keyStore;
        private final X509TrustManager defaultTrustManager;
        private final X509TrustManager trustManager;

        public CustomCertTrustManager(KeyStore keyStore) {
            this.keyStore = keyStore;
            // get the default trust manager
            TrustManagerFactory defaultTrustManagerFactory;
            try {
                defaultTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                defaultTrustManagerFactory.init((KeyStore) null);
            } catch (NoSuchAlgorithmException | KeyStoreException e) {
                throw new TrustManagerLoadFailedException(e);
            }
            X509TrustManager defaultTrustManager = null;
            for (TrustManager tm : defaultTrustManagerFactory.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    defaultTrustManager = (X509TrustManager) tm;
                    break;
                }
            }
            if (defaultTrustManager == null) {
                throw new TrustManagerLoadFailedException("Default X509TrustManager not found");
            }
            this.defaultTrustManager = defaultTrustManager;

            TrustManagerFactory trustManagerFactory;
            try {
                trustManagerFactory = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
                trustManagerFactory.init(keyStore);
            } catch (NoSuchAlgorithmException | NoSuchProviderException | KeyStoreException e) {
                throw new TrustManagerLoadFailedException(e);
            }

            X509TrustManager trustManager = null;
            for (TrustManager tm : trustManagerFactory.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    trustManager = (X509TrustManager) tm;
                    break;
                }
            }
            if (trustManager == null) {
                throw new TrustManagerLoadFailedException("X509TrustManager for user keystore not found");
            }
            this.trustManager = trustManager;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                trustManager.checkClientTrusted(chain, authType);
            } catch (CertificateException e) {
                defaultTrustManager.checkClientTrusted(chain, authType);
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                trustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException e) {
                defaultTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] defaultAcceptedIssuers = defaultTrustManager.getAcceptedIssuers();
            X509Certificate[] acceptedIssuers = trustManager.getAcceptedIssuers();
            X509Certificate[] result = new X509Certificate[defaultAcceptedIssuers.length + acceptedIssuers.length];
            System.arraycopy(defaultAcceptedIssuers, 0, result, 0, defaultAcceptedIssuers.length);
            System.arraycopy(acceptedIssuers, 0, result, defaultAcceptedIssuers.length, acceptedIssuers.length);
            return result;
        }
    }

}
