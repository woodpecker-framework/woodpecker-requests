package me.gv7.woodpecker.requests.body;

import me.gv7.woodpecker.requests.HttpHeaders;
import me.gv7.woodpecker.requests.json.JsonLookup;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author Liu Dong
 */
class JsonRequestBody<T> extends RequestBody<T> {

    private static final long serialVersionUID = 890531624817102489L;

    JsonRequestBody(T body) {
        super(body, HttpHeaders.CONTENT_TYPE_JSON, true);
    }

    @Override
    public void writeBody(OutputStream out, Charset charset) throws IOException {
        try (Writer writer = new OutputStreamWriter(out, charset)) {
            JsonLookup.getInstance().lookup().marshal(writer, body());
        }
    }
}
