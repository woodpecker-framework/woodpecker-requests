package me.gv7.woodpecker.requests.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Json provider
 *
 * @author Liu Dong
 */
public interface JsonProcessor {

    /**
     * Serialize value to json, and writeTo to writer
     */
    void marshal(Writer writer, Object value) throws IOException;

    /**
     * Deserialize json from input stream, with charset and type.
     */
    <T> T unmarshal(InputStream inputStream, Charset charset, Type type) throws IOException;

}
