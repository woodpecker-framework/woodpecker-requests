package me.gv7.woodpecker.requests;

import me.gv7.woodpecker.requests.config.CustomHttpHeaderConfig;
import me.gv7.woodpecker.requests.utils.HttpConfigUtil;
import net.dongliu.commons.collection.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
        HttpConfigUtil.setCustomHttpHeaderConfig(newHeaders,false);
        LinkedHashMap<String,String> headers = new LinkedHashMap<>();
        headers.put("aaa","aaa");
        RequestBuilder requestBuilder = Requests.get("http://woodpecker.gv7.me/index").headers(headers);
        requestBuilder.build();

        assertEquals(null,requestBuilder.getHeader("Host"));
        assertEquals("aaa",requestBuilder.getHeader("aaa"));
        assertEquals("bbb",requestBuilder.getHeader("bbb"));
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
        HttpConfigUtil.setCustomHttpHeaderConfig(newHeaders,true);
        LinkedHashMap<String,String> headers = new LinkedHashMap<>();
        headers.put("aaa","aaa");
        RequestBuilder requestBuilder = Requests.get("http://woodpecker.gv7.me/index").headers(headers);
        requestBuilder.build();

        assertEquals("overwrite.com",requestBuilder.getHeader("Host"));
        assertEquals("overwrite",requestBuilder.getHeader("aaa"));
        assertEquals("bbb",requestBuilder.getHeader("bbb"));
    }
}