package me.gv7.woodpecker.requests.json;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Liu Dong
 */
public class JsonLookupTest {

    @Test
    public void test() {
        JsonLookup lookup = JsonLookup.getInstance();
        assertTrue(lookup.hasFastJson());
        assertTrue(lookup.hasGson());
        assertTrue(lookup.hasJackson());
    }
}