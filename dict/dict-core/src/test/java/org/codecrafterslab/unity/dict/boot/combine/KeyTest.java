package org.codecrafterslab.unity.dict.boot.combine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {
    private Key idKey1;
    private Key idKey2;
    private Key idKey3;
    private Key idKey4;
    private Key codeKey1;
    private Key codeKey2;
    private Key codeKey3;

    @BeforeEach
    void setUp() {
        idKey1 = Key.of(Scope.ID, true);
        idKey2 = Key.of(Scope.ID, false);
        idKey3 = Key.of(Scope.ID);
        idKey4 = Key.of(Scope.ID, Collections.singletonMap(Scope.ID, ""));

        codeKey1 = Key.of(Scope.CODE, true);
        codeKey2 = Key.of(Scope.CODE, false);
        codeKey3 = Key.of(Scope.CODE);
    }

    @Test
    void testGetScope() {
        assertEquals(Scope.ID, idKey1.getScope());
        assertEquals(Scope.ID, idKey2.getScope());
        assertEquals(Scope.ID, idKey3.getScope());
        assertEquals(Scope.ID, idKey4.getScope());

        assertEquals(Scope.CODE, codeKey1.getScope());
        assertEquals(Scope.CODE, codeKey2.getScope());
        assertEquals(Scope.CODE, codeKey3.getScope());
    }

    @Test
    void testGetValue() {
        assertEquals(Scope.ID.getCode().toLowerCase(), idKey1.getValue());
        assertNull(idKey2.getValue());
        assertNull(idKey3.getValue());
        assertEquals("", idKey4.getValue());

        assertEquals(Scope.CODE.getCode().toLowerCase(), codeKey1.getValue());
        assertNull(codeKey2.getValue());
        assertNull(codeKey3.getValue());
    }

    @Test
    void testEquals() {
        assertNotEquals(idKey1, idKey2);
        assertNotEquals(idKey1, idKey3);
        assertNotEquals(idKey1, idKey4);

        assertEquals(idKey2, idKey3);
        assertNotEquals(idKey2, idKey4);
        assertNotEquals(idKey3, idKey4);

        assertNotEquals(codeKey1, codeKey2);
        assertEquals(codeKey2, codeKey3);
    }

    @Test
    void testHashCode() {
        assertNotEquals(idKey1.hashCode(), idKey2.hashCode());
        assertNotEquals(idKey1.hashCode(), codeKey1.hashCode());

        assertEquals(idKey2.hashCode(), idKey3.hashCode());
        assertNotEquals(idKey2.hashCode(), idKey4.hashCode());
        assertNotEquals(idKey3.hashCode(), idKey4.hashCode());
    }

    @Test
    void testCombine() {
        assertEquals(idKey1, idKey1.combine(idKey2));
        assertEquals(idKey1, idKey2.combine(idKey1));

        assertEquals(idKey2, idKey2.combine(idKey3));
        assertEquals(idKey3, idKey2.combine(idKey3));

        assertEquals(idKey2, idKey3.combine(idKey2));
        assertEquals(idKey3, idKey3.combine(idKey2));
    }

    @Test
    void testListKeyCombine() {
        List<Key> keys1 = Arrays.asList(idKey1, idKey2, idKey3, idKey4, codeKey3);
        List<Key> keys2 = Arrays.asList(Key.of(Scope.DESCRIPTION), Key.of(Scope.VALUE, true),
                Key.of(Scope.VALUE, "val"));
        List<Key> combine = Key.combine(keys1, keys2);
        assertFalse(combine.isEmpty());
    }

    @Test
    public void TestTrace() {
        List<Integer> collect = Stream.of("beijing", "tianjin", "shanghai", "wuhan")
                .map(String::length)
                .filter(e -> e > 5)
                .collect(Collectors.toList());


    }
}
