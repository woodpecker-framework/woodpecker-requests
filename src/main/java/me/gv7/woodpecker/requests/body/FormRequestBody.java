package me.gv7.woodpecker.requests.body;

import me.gv7.woodpecker.requests.HttpHeaders;
import me.gv7.woodpecker.requests.utils.URLUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * @author Liu Dong
 */
class FormRequestBody extends RequestBody<Collection<? extends Map.Entry<String, ?>>> {
    private static final long serialVersionUID = 6322052512305107136L;

    FormRequestBody(Collection<? extends Map.Entry<String, ?>> body) {
        super(body, HttpHeaders.CONTENT_TYPE_FORM_ENCODED, true);
    }

    @Override
    public void writeBody(OutputStream out, Charset charset) throws IOException {
        String content = URLUtils.encodeForms(URLUtils.toStringParameters(body()), charset);
        try (Writer writer = new OutputStreamWriter(out, charset)) {
            writer.write(content);
        }
    }

}
