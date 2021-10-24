package me.gv7.woodpecker.requests;

import me.gv7.woodpecker.requests.config.HttpConfigManager;
import net.dongliu.commons.collection.Lists;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Dong
 */
public class HeadersTest {
    @Test
    public void getHeaders() {
        Headers headers = new Headers(Arrays.asList(
                new Header("Location", "www"),
                new Header("Location", "www2"),
                new Header("Content-Length", "100")
        ));
        assertEquals(Lists.of("www", "www2"), headers.getHeaders("Location"));
    }

    @Test
    public void getHeader() {
        Headers headers = new Headers(Lists.of(
                new Header("Location", "www"),
                new Header("Location", "www2"),
                new Header("Content-Length", "100")
        ));
        assertEquals("www", headers.getHeader("Location"));
        assertEquals("www", headers.getHeader("location"));
    }

    @Test
    public void getLongHeader() {
        Headers headers = new Headers(Lists.of(
                new Header("Location", "www"),
                new Header("Location", "www2"),
                new Header("Content-Length", "100")
        ));
        assertEquals(100, headers.getLongHeader("Content-Length", -1));
    }

    /**
     * 测试不覆盖模式下，Host以及用户设置的头部是否保持原样
     */
    @Test
    public void setNoOverwriteHeader(){
        LinkedHashMap<String,String> newHeaders = new LinkedHashMap<>();
        newHeaders.put("Host","overwrite.com");
        newHeaders.put("aaa","overwrite");
        newHeaders.put("bbb","bbb");
        HttpConfigManager.setCustomHttpHeaderConfig(newHeaders,false);
        LinkedHashMap<String,String> headers = new LinkedHashMap<>();
        headers.put("aaa","aaa");
        headers.put("ccc","ccc");
        RequestBuilder requestBuilder = Requests.get("http://woodpecker.gv7.me/index").headers(headers);
        requestBuilder.build();

        assertEquals(null,requestBuilder.getHeader("Host"));
        assertEquals("aaa",requestBuilder.getHeader("aaa"));
        assertEquals("bbb",requestBuilder.getHeader("bbb"));
        assertEquals("ccc",requestBuilder.getHeader("ccc"));
    }

    /**
     * 测试覆盖模式下是否覆盖成功
     */
    @Test
    public void setOverwriteHeader(){
        LinkedHashMap<String,String> newHeaders = new LinkedHashMap<>();
        newHeaders.put("Host","overwrite.com");
        newHeaders.put("aaa","overwrite");
        newHeaders.put("bbb","bbb");
        HttpConfigManager.setCustomHttpHeaderConfig(newHeaders,true);
        LinkedHashMap<String,String> headers = new LinkedHashMap<>();
        headers.put("aaa","aaa");
        headers.put("ccc","ccc");
        RequestBuilder requestBuilder = Requests.get("http://woodpecker.gv7.me/index").headers(headers);
        requestBuilder.build();

        assertEquals("overwrite.com",requestBuilder.getHeader("Host"));
        assertEquals("overwrite",requestBuilder.getHeader("aaa"));
        assertEquals("bbb",requestBuilder.getHeader("bbb"));
        assertEquals("ccc",requestBuilder.getHeader("ccc")); // 测试是否用户未被覆盖的header否否保持
    }

    /**
     * 测试新增多个header是否会覆盖
     */
    @Test
    public void setManyHeader(){
        HashMap<String, String> h = new HashMap<>();
        h.put("header1","header1");
        HashMap<String, String> h2 = new HashMap<String, String>();
        h2.put("header2","header2");
        HashMap<String, String> h3 = new HashMap<String, String>();
        h3.put("header3","header3");
        String url = "http://www.baidu.com";
        RequestBuilder requestBuilder = Requests.get(url)
                .headers(h) // header1
                .headers(h2) // header2
                .headers(h3.entrySet()) // header3
                .headers(Lists.of(new Header("header4","header4"))); // header4
        assertEquals("header1",requestBuilder.getHeader("header1"));
        assertEquals("header2",requestBuilder.getHeader("header2"));
        assertEquals("header3",requestBuilder.getHeader("header3"));
        assertEquals("header4",requestBuilder.getHeader("header4"));
    }

}