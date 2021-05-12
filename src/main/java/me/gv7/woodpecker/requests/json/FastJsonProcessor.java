//package me.gv7.woodpecker.requests.json;
//
//import com.alibaba.fastjson.JSON;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.Writer;
//import java.lang.reflect.Type;
//import java.nio.charset.Charset;
//
///**
// * @author Liu Dong
// */
//public class FastJsonProcessor implements JsonProcessor {
//    @Override
//    public void marshal(Writer writer, Object value) {
//        JSON.writeJSONString(writer, value);
//    }
//
//    @Override
//    public <T> T unmarshal(InputStream inputStream, Charset charset, Type type) throws IOException {
//        return JSON.parseObject(inputStream, charset, type);
//    }
//}
