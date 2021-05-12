package me.gv7.woodpecker.requests.body;

import me.gv7.woodpecker.requests.HttpHeaders;
import net.dongliu.commons.io.InputStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Liu Dong
 * @deprecated use {@link InputStreamSupplierRequestBody} instead
 */
@Deprecated
class InputStreamRequestBody extends RequestBody<InputStream> {
    private static final long serialVersionUID = -2463504960044237751L;

    InputStreamRequestBody(InputStream body) {
        super(body, HttpHeaders.CONTENT_TYPE_BINARY, false);
    }

    @Override
    public void writeBody(OutputStream out, Charset charset) throws IOException {
        try (InputStream in = body()) {
            InputStreams.transferTo(in, out);
        }
    }
}
