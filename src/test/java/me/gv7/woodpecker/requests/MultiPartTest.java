package me.gv7.woodpecker.requests;

import me.gv7.woodpecker.requests.body.Part;
import org.junit.Test;

import java.io.File;

public class MultiPartTest {

    @Test
    public void testOf() throws Exception {
        Part multiPart = Part.file("writeTo", new File("MultiPartTest.java"));
    }
}