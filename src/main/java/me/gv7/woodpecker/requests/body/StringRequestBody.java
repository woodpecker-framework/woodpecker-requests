package me.gv7.woodpecker.requests.body;

import me.gv7.woodpecker.requests.HttpHeaders;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author Liu Dong
 */
class StringRequestBody extends RequestBody<String> {
    private static final long serialVersionUID = -1542159158991437897L;

    StringRequestBody(String body) {
        super(body, HttpHeaders.CONTENT_TYPE_TEXT, true);
    }

    @Override
    public void writeBody(OutputStream out, Charset charset) throws IOException {
        try (Writer writer = new OutputStreamWriter(out, charset)) {
            writer.write(body());
        }
    }
}
