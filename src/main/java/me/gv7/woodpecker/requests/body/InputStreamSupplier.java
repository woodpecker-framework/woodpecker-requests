package me.gv7.woodpecker.requests.body;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Which can provider a input stream.
 */
public interface InputStreamSupplier extends Supplier<InputStream> {
    /**
     * Return a InputStream.
     * Every call to this method, should return a new InputStream, all InputStreams returned should contains the same data.
     *
     * @return A InputStream
     */
    InputStream get();
}
