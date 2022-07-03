package com.tvv.service.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHashTest {

    @Test
    void testGetHashString() {
        assertEquals(StringHash.getHashString("1111"),
                "ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
        assertEquals(StringHash.getHashString("admin"),
                "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");
        assertEquals(StringHash.getHashString("admin123"),
                "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9");
        assertThrows(IllegalArgumentException.class, ()->StringHash.getHashString(null));
    }
}