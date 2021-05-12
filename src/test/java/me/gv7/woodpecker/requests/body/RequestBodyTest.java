package me.gv7.woodpecker.requests.body;

import net.dongliu.commons.collection.Lists;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RequestBodyTest {
    @Test
    public void json() throws Exception {
        RequestBody<List<String>> body = RequestBody.json(Lists.of("1", "2", "3"));
        assertEquals("application/json", body.contentType());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        body.writeBody(bos, StandardCharsets.UTF_8);
        String str = bos.toString("UTF-8");
        assertEquals("[\"1\",\"2\",\"3\"]", str);
    }

}