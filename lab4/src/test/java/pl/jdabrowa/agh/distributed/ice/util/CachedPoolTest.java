package pl.jdabrowa.agh.distributed.ice.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class CachedPoolTest {

    @Test
    public void should() {

        // given
        CachedPool<String> instance = new CachedPool<>(String::valueOf);

        // when

        // then
        String one = instance.get(1);
        assertEquals("1", one);
        String two = instance.get(2);
        assertEquals("2", two);
        String three = instance.get(3);
        assertEquals("3", three);
        String four = instance.get(4);
        assertEquals("4", four);
        assertSame(one, instance.get(1));
        assertEquals(two, instance.get(2));
    }

}