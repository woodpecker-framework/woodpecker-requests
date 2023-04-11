package me.gv7.woodpecker.requests;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author c0ny1
 * @date 2023/04/11 16:32:50
 **/
public class ChunkedHeaderTest {
    /**
     * 问题：无法发送Request header Transfer-Encoding: chunked
     * 原因：经过测试会在分块传输头会在sun.net.www.protocol.http.HttpURLConnection#writeRequests()处被删除。
     * 注意：
     *      1.不要用burp抓包测试，burp似乎对分块进行合并，请使用wireshark。
     *      2.在me.gv7.woodpecker.requests.executor.URLConnectionExecutor#doRequest(me.gv7.woodpecker.requests.Request)处
     *        加入conn.setChunkedStreamingMode(1);可以发送Transfer-Encoding: chunked，但是没发完全控制body。要支持分块传输还需要深入深入研究。
     *
     */
    @Test
    public void sendChunkedHeader(){
        Map<String,String> header = new HashMap<String,String>();
        header.put("Transfer-Encoding","chunked");
        header.put("aaa","bbb");
        Requests.post("http://127.0.0.1:1664/").headers(header).body("2\r\nxxs3\r\ndwe").proxy(Proxies.httpProxy("127.0.0.1",1664)).send();
    }
}
