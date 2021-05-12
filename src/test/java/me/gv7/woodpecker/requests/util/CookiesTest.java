package me.gv7.woodpecker.requests.util;

import me.gv7.woodpecker.requests.Cookie;
import me.gv7.woodpecker.requests.utils.Cookies;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Liu Dong
 */
public class CookiesTest {
    @Test
    public void isSubDomain() throws Exception {
        assertTrue(Cookies.isDomainSuffix("www.baidu.com", "baidu.com"));
        assertTrue(Cookies.isDomainSuffix("baidu.com", "baidu.com"));
        assertFalse(Cookies.isDomainSuffix("a.com", "baidu.com"));
        assertFalse(Cookies.isDomainSuffix("ww.a.com", "baidu.com"));
    }

    @Test
    public void effectivePath() {
        assertEquals("/test/", Cookies.calculatePath("/test/123"));
        assertEquals("/", Cookies.calculatePath("/"));
        assertEquals("/", Cookies.calculatePath(""));
    }

    @Test
    public void isIP() {
        assertTrue(Cookies.isIP("202.38.64.10"));
        assertTrue(Cookies.isIP("2001:0DB8:0000:0023:0008:0800:200C:417A"));
        assertTrue(Cookies.isIP("2001:DB8:0:23:8:800:200C:417A"));
        assertTrue(Cookies.isIP("FF01:0:0:0:0:0:0:1101"));
        assertTrue(Cookies.isIP("::1"));
        assertTrue(Cookies.isIP("::"));
        assertTrue(Cookies.isIP("::192.168.0.1"));
        assertTrue(Cookies.isIP("::FFFF:192.168.0.1"));
        assertFalse(Cookies.isIP("202.test.com"));
        assertFalse(Cookies.isIP("163.com"));
        assertFalse(Cookies.isIP("163.ac"));
    }

    @Test
    public void match() throws Exception {
        Cookie cookie = new Cookie("test.com", "/", "test", "value", 0, false, false);
        assertTrue(Cookies.match(cookie, "http", "test.com", "/test/"));
        assertTrue(Cookies.match(cookie, "http", "www.test.com", "/test/"));

        // https
        cookie = new Cookie("test.com", "/", "test", "value", 0, true, false);
        assertFalse(Cookies.match(cookie, "http", "test.com", "/test/"));
        assertTrue(Cookies.match(cookie, "https", "test.com", "/test/"));

        cookie = new Cookie("test.com", "/", "test", "value", 0, false, true);
        assertTrue(Cookies.match(cookie, "http", "test.com", "/test/"));
        assertFalse(Cookies.match(cookie, "http", "www.test.com", "/test/"));

        cookie = new Cookie("test.com", "/test/", "test", "value", 0, false, false);
        assertFalse(Cookies.match(cookie, "http", "test.com", "/"));
        assertTrue(Cookies.match(cookie, "http", "www.test.com", "/test/"));

        cookie = new Cookie("202.38.64.10", "/", "test", "value", 0, false, false);
        assertFalse(Cookies.match(cookie, "http", "38.64.10", "/"));
        assertTrue(Cookies.match(cookie, "http", "202.38.64.10", "/"));
    }

    @Test
    public void parseCookie() throws Exception {
        String cookieStr = "__bsi=11937048251853133038_00_0_I_R_181_0303_C02F_N_I_I_0;" +
                " expires=Thu, 16-Mar-17 03:39:29 GMT; domain=www.baidu.com; path=/";
        Cookie cookie = Cookies.parseCookie(cookieStr, "www.baidu.com", "/");
        assertNotNull(cookie);
        assertFalse(cookie.hostOnly());
        assertEquals("www.baidu.com", cookie.domain());
        assertEquals(1489635569000L, cookie.expiry());
        assertEquals("11937048251853133038_00_0_I_R_181_0303_C02F_N_I_I_0", cookie.value());
        assertEquals("/", cookie.path());
        assertEquals("__bsi", cookie.name());


        cookieStr = "V2EX_TAB=\"2|1:0|10:1489639030|8:V2EX_TAB|4:YWxs|94149dfee574a182c7a43cbcb752230e9e09ca44173293ca6ab446e9e1754598\";" +
                " expires=Thu, 30 Mar 2017 04:37:10 GMT; httponly; Path=/";
        cookie = Cookies.parseCookie(cookieStr, "www.v2ex.com", "/");
        assertNotNull(cookie);
        assertEquals("www.v2ex.com", cookie.domain());
        assertTrue(cookie.hostOnly());
        assertEquals(1490848630000L, cookie.expiry());
        assertEquals("/", cookie.path());
        assertEquals("V2EX_TAB", cookie.name());

        cookieStr = "YF-V5-G0=a2489c19ecf98bbe86a7bf6f0edcb071;Path=/";
        cookie = Cookies.parseCookie(cookieStr, "weibo.com", "/");
        assertNotNull(cookie);
        assertTrue(cookie.hostOnly());
        assertEquals("weibo.com", cookie.domain());
        assertEquals(0, cookie.expiry());

        cookieStr = "ALF=1521175171; expires=Friday, 16-Mar-2018 04:39:31 GMT; path=/; domain=.sina.com.cn";
        cookie = Cookies.parseCookie(cookieStr, "login.sina.com.cn", "/sso/");
        assertNotNull(cookie);
        assertEquals("sina.com.cn", cookie.domain());
        assertEquals(1521175171000L, cookie.expiry());
        assertEquals("/", cookie.path());


        cookie = Cookies.parseCookie("skey=@k4bPcIye6; PATH=/; DOMAIN=qq.com;", "ssl.ptlogin2.qq.com", "/");
        assertNotNull(cookie);
        assertEquals("qq.com", cookie.domain());
    }
}