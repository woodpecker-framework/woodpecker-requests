package me.gv7.woodpecker.requests;

import me.gv7.woodpecker.requests.exception.TrustManagerLoadFailedException;
import net.dongliu.commons.io.Closeables;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeyStores {

    /**
     * Load keystore from file.
     */
    public static KeyStore load(String path, char[] password) {
        try {
            return load(new FileInputStream(path), password);
        } catch (FileNotFoundException e) {
            throw new TrustManagerLoadFailedException(e);
        }
    }

    /**
     * Load keystore from InputStream, close the stream after load succeed or failed.
     */
    public static KeyStore load(InputStream in, char[] password) {

        try {
            KeyStore myTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            myTrustStore.load(in, password);
            return myTrustStore;
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            throw new TrustManagerLoadFailedException(e);
        } finally {
            Closeables.closeQuietly(in);
        }
    }
}
