package me.gv7.woodpecker.requests.util;

import net.dongliu.commons.collection.Lists;
import me.gv7.woodpecker.requests.Parameter;
import me.gv7.woodpecker.requests.utils.URLUtils;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class URLUtilsTest {
    @Test
    public void joinUrl() throws Exception {
        List<Parameter<String>> empty = Lists.of();
        assertEquals("http://www.test.com/", URLUtils.joinUrl(new URL("http://www.test.com/"),
                empty, UTF_8).toExternalForm());
        assertEquals("http://www.test.com/path", URLUtils.joinUrl(new URL("http://www.test.com/path"),
                empty, UTF_8).toExternalForm());

        assertEquals("http://www.test.com/path?t=v", URLUtils.joinUrl(new URL("http://www.test.com/path"),
                Lists.of(Parameter.of("t", "v")), UTF_8).toExternalForm());
        assertEquals("http://www.test.com/path?s=t&t=v", URLUtils.joinUrl(new URL("http://www.test.com/path?s=t"),
                Lists.of(Parameter.of("t", "v")), UTF_8).toExternalForm());
        assertEquals("http://www.test.com/path?t=v", URLUtils.joinUrl(new URL("http://www.test.com/path?"),
                Lists.of(Parameter.of("t", "v")), UTF_8).toExternalForm());
        assertEquals("http://www.test.com/path?t=v#seg", URLUtils.joinUrl(new URL("http://www.test.com/path#seg"),
                Lists.of(Parameter.of("t", "v")), UTF_8).toExternalForm());
        assertEquals("http://www.test.com/path?t=v#", URLUtils.joinUrl(new URL("http://www.test.com/path#"),
                Lists.of(Parameter.of("t", "v")), UTF_8).toExternalForm());
    }

}