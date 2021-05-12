package me.gv7.woodpecker.requests.body;

import me.gv7.woodpecker.requests.HttpHeaders;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Liu Dong
 */
class BytesRequestBody extends RequestBody<byte[]> {
    private static final long serialVersionUID = 5476648279904264255L;

    BytesRequestBody(byte[] body) {
        super(body, HttpHeaders.CONTENT_TYPE_BINARY, false);
    }

    @Override public void writeBody(OutputStream out, Charset charset) throws IOException {
        out.write(body());
    }
}
