package me.gv7.woodpecker.requests;

import net.dongliu.commons.collection.Lists;
import org.junit.Test;

import java.util.Arrays;

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

}