package me.gv7.woodpecker.requests;

import me.gv7.woodpecker.requests.mock.MockServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SessionTest {

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
    public void testSession() {
        Session session = Requests.session();
        String response = session.get("http://127.0.0.1:8080").send().readToText();
        assertTrue(!response.isEmpty());
    }
}