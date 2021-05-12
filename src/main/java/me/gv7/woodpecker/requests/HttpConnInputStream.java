package me.gv7.woodpecker.requests;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * A InputStream delegate all method to http response input stream, and release http connection(to connection pool or close) When this input stream closed.
 */
class HttpConnInputStream extends FilterInputStream {

    private final HttpURLConnection conn;

    protected HttpConnInputStream(InputStream in, HttpURLConnection conn) {
        super(in);
        this.conn = conn;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            conn.disconnect();
        }

    }
}
