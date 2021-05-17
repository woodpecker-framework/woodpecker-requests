package me.gv7.woodpecker.requests;

import net.dongliu.commons.collection.Lists;
import me.gv7.woodpecker.requests.body.InputStreamSupplier;
import me.gv7.woodpecker.requests.body.Part;
import me.gv7.woodpecker.requests.json.TypeInfer;
import me.gv7.woodpecker.requests.mock.MockServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestsTest {

    private static MockServer server = new MockServer();

    @BeforeClass
    public static void init() {
        server.start();
    }

    @AfterClass
    public static void destroy() {
        server.stop();
    }

    @Test
    public void testGet() {
        String resp = Requests.get("http://127.0.0.1:8080")
                .requestCharset(StandardCharsets.UTF_8).send().readToText();
        assertFalse(resp.isEmpty());

        resp = Requests.get("http://127.0.0.1:8080").send().readToText();
        assertFalse(resp.isEmpty());

        // get with params
        Map<String, String> map = new HashMap<>();
        map.put("wd", "test");
        resp = Requests.get("http://127.0.0.1:8080").params(map).send().readToText();
        assertFalse(resp.isEmpty());
        assertTrue(resp.contains("wd=test"));
    }

    @Test
    public void testHead() {
        RawResponse resp = Requests.head("http://127.0.0.1:8080")
                .requestCharset(StandardCharsets.UTF_8).send();
        assertEquals(200, resp.statusCode());
        String statusLine = resp.statusLine();
        assertEquals("HTTP/1.1 200 OK", statusLine);
        String text = resp.readToText();
        assertTrue(text.isEmpty());
    }

    @Test
    public void testPost() {
        // form encoded post
        String text = Requests.post("http://127.0.0.1:8080/post")
                .body(Parameter.of("wd", "test"))
                .send().readToText();
        assertTrue(text.contains("wd=test"));
    }

    @Test
    public void testCookie() {
        Response<String> response = Requests.get("http://127.0.0.1:8080/cookie")
                .cookies(Parameter.of("test", "value")).send().toTextResponse();
        boolean flag = false;
        for (Cookie cookie : response.cookies()) {
            if (cookie.name().equals("test")) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void testBasicAuth() {
        Response<String> response = Requests.get("http://127.0.0.1:8080/basicAuth")
                .basicAuth("test", "password")
                .send().toTextResponse();
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testRedirect() {
        Response<String> resp = Requests.get("http://127.0.0.1:8080/redirect").userAgent("my-user-agent")
                .send().toTextResponse();
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("/redirected"));
        assertTrue(resp.body().contains("my-user-agent"));
    }

    @Test
    public void testMultiPart() {
        String body = Requests.post("http://127.0.0.1:8080/multi_part")
                .multiPartBody(Part.file("writeTo", "keystore", new InputStreamSupplier() {
                    @Override
                    public InputStream get() {
                        return this.getClass().getResourceAsStream("/keystore");
                    }
                }).contentType("application/octem-stream"))
                .send().readToText();
        assertTrue(body.contains("writeTo"));
        assertTrue(body.contains("application/octem-stream"));
    }


    @Test
    public void testMultiPartText() {
        String body = Requests.post("http://127.0.0.1:8080/multi_part")
                .multiPartBody(Part.text("test", "this is test value"))
                .send().readToText();
        assertTrue(body.contains("this is test value"));
        assertTrue(!body.contains("plain/text"));
    }

    @Test
    public void sendJson() {
        String text = Requests.post("http://127.0.0.1:8080/echo_body").jsonBody(Lists.of(1, 2, 3))
                .send().readToText();
        assertTrue(text.startsWith("["));
        assertTrue(text.endsWith("]"));
    }

    @Test
    public void receiveJson() {
        List<Integer> list = Requests.post("http://127.0.0.1:8080/echo_body").jsonBody(Lists.of(1, 2, 3))
                .send().readToJson(new TypeInfer<List<Integer>>() {
                });
        assertEquals(3, list.size());
    }

    @Test
    public void sendHeaders() {
        RequestBuilder.DEBUG = true;
        String text = Requests.get("http://gv7.me/echo_header")
                .headers(new Header("Host", "www.test.com"), new Header("TestHeader", 1))
                .proxy(Proxies.httpProxy("127.0.0.1",8080))
                .send().readToText();
        assertTrue(text.contains("Host: www.test.com"));
        assertTrue(text.contains("TestHeader: 1"));
    }

    @Test
    public void sendHeaders2() {
        RequestBuilder.DEBUG = true;
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("TestHeader","1");
        String text = Requests.get("http://gv7.me/echo_header")
                .headers(headers)
                .proxy(Proxies.httpProxy("127.0.0.1",8080))
                .send().readToText();
        assertTrue(text.contains("Host: www.test.com"));
        assertTrue(text.contains("TestHeader: 1"));
    }

    @Test
    public void testHttps() {
        Response<String> response = Requests.get("https://127.0.0.1:8443/https")
                .verify(false).send().toTextResponse();
        assertEquals(200, response.statusCode());


        KeyStore keyStore = KeyStores.load(this.getClass().getResourceAsStream("/keystore"), "123456".toCharArray());
        response = Requests.get("https://127.0.0.1:8443/https")
                .keyStore(keyStore)
                .send().toTextResponse();
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testInterceptor() {
        final long[] statusCode = {0};
        Interceptor interceptor = (target, request) -> {
            RawResponse response = target.proceed(request);
            statusCode[0] = response.statusCode();
            return response;
        };

        String text = Requests.get("http://127.0.0.1:8080/echo_header")
                .interceptors(interceptor)
                .send().readToText();
        assertFalse(text.isEmpty());
        assertTrue(statusCode[0] > 0);
    }
}