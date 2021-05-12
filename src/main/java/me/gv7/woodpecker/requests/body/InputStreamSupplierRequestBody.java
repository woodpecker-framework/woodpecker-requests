package me.gv7.woodpecker.requests.body;

import me.gv7.woodpecker.requests.HttpHeaders;
import net.dongliu.commons.io.InputStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Liu Dong
 */
class InputStreamSupplierRequestBody extends RequestBody<InputStreamSupplier> {
    private static final long serialVersionUID = -2463504912342237751L;

    InputStreamSupplierRequestBody(InputStreamSupplier body) {
        super(body, HttpHeaders.CONTENT_TYPE_BINARY, false);
    }

    @Override
    public void writeBody(OutputStream out, Charset charset) throws IOException {
        try (InputStream in = body().get()) {
            InputStreams.transferTo(in, out);
        }
    }
}
