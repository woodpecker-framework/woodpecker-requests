//package me.gv7.woodpecker.requests.json;
//
//import org.junit.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.StringWriter;
//import java.nio.charset.StandardCharsets;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * @author Liu Dong
// */
//public class FastJsonProcessorTest {
//    @Test
//    public void marshal() throws Exception {
//        FastJsonProcessor jsonProvider = new FastJsonProcessor();
//        try (StringWriter writer = new StringWriter()) {
//            jsonProvider.marshal(writer, "test");
//            String s = writer.toString();
//            assertEquals("\"test\"", s);
//        }
//    }
//
//    @Test
//    public void unmarshal() throws Exception {
//        FastJsonProcessor jsonProvider = new FastJsonProcessor();
//        String str = jsonProvider.unmarshal(new ByteArrayInputStream("\"test\"".getBytes()), StandardCharsets.UTF_8,
//                String.class);
//        assertEquals("test", str);
//    }
//
//}