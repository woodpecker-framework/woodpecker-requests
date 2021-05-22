package me.gv7.woodpecker.requests;

import me.gv7.woodpecker.requests.config.HttpConfigManager;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class IgnoreHttpConfigTest {
    /**
     * 设置开启忽略Http配置模式
     */
    @Test
    public void ignoreHttpConfigTest(){
        LinkedHashMap<String,String> newHeaders = new LinkedHashMap<>();
        newHeaders.put("aaa","overwrite");
        newHeaders.put("bbb","bbb");
        HttpConfigManager.setCustomHttpHeaderConfig(newHeaders,true);

        LinkedHashMap<String,String> headers = new LinkedHashMap<>();
        headers.put("aaa","aaa");
        headers.put("ccc","ccc");
        RequestBuilder requestBuilder = Requests.get("http://woodpecker.gv7.me/index").headers(headers).ignoreHttpConfig(true);
        requestBuilder.build();

        assertEquals("aaa",requestBuilder.getHeader("aaa"));
        assertEquals(null,requestBuilder.getHeader("bbb"));
        assertEquals("ccc",requestBuilder.getHeader("ccc")); // 测试是否用户未被覆盖的header否否保持
    }

    /**
     * 设置关闭忽略Http配置模式
     */
    @Test
    public void notIgnoreHttpConfigTest(){
        LinkedHashMap<String,String> newHeaders = new LinkedHashMap<>();
        newHeaders.put("aaa","overwrite");
        newHeaders.put("bbb","bbb");
        HttpConfigManager.setCustomHttpHeaderConfig(newHeaders,true);

        LinkedHashMap<String,String> headers = new LinkedHashMap<>();
        headers.put("aaa","aaa");
        headers.put("ccc","ccc");
        RequestBuilder requestBuilder = Requests.get("http://woodpecker.gv7.me/index").headers(headers).ignoreHttpConfig(false);
        requestBuilder.build();

        assertEquals("overwrite",requestBuilder.getHeader("aaa"));
        assertEquals("bbb",requestBuilder.getHeader("bbb"));
        assertEquals("ccc",requestBuilder.getHeader("ccc")); // 测试是否用户未被覆盖的header否否保持
    }
}
