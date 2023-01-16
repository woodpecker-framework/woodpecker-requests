package me.gv7.woodpecker.requests;

import me.gv7.woodpecker.requests.config.HttpConfigManager;
import me.gv7.woodpecker.requests.mock.MockServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.net.Proxy;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Dong
 */
@Ignore
public class RequestsProxyTest {

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
    public void testHttpProxy() {
        // http proxy with redirect
        RawResponse response = Requests
                .get("https://www.google.com")
                .proxy(Proxies.httpProxy("127.0.0.1", 1081))
                .send();
        response.close();
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testSocksProxy() {
        // socks proxy with redirect
        RawResponse response = Requests
                .get("https://www.google.com")
                .proxy(Proxies.socksProxy("127.0.0.1", 1080))
                .send();
        response.close();
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testHttpProxy1() throws Exception {
        HttpConfigManager.setProxyConfig(true
                ,"http"
                ,"127.0.0.1"
                ,8080
                ,null
                ,null);
        RawResponse response = Requests.method("GET","http://wwww.baidu.com/xxx").proxy(Proxy.NO_PROXY).send();
        System.out.println(response.readToText());
    }
}
