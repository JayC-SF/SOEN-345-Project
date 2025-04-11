package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@code ConstantInitializer}.
 *
 * @version $Id$
 */
public class ConstantInitializerTest {
    /** Constant for the object managed by the initializer. */
    private static final Integer VALUE = 42;

    /** The initializer to be tested. */
    private ConstantInitializer<Integer> init;

    @Before
    public void setUp() throws Exception {
        init = new ConstantInitializer<Integer>(VALUE);
    }

    @Test
    public void testGet() throws ConcurrentException {
        assertEquals("Wrong value", VALUE, init.get());
    }

    @Test
    public void testEqualsTrue() {
        ConstantInitializer<Integer> other = new ConstantInitializer<Integer>(VALUE);
        assertEquals("Initializers should be equal", init, other);
        assertEquals("Hash codes should match", init.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsFalse() {
        ConstantInitializer<Integer> other = new ConstantInitializer<Integer>(99);
        assertFalse("Initializers should not be equal", init.equals(other));
    }

    @Test
    public void testEqualsNull() {
        assertFalse("Should not be equal to null", init.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse("Should not be equal to different class", init.equals("someString"));
    }

    @Test
    public void testToString() {
        String str = init.toString();
        assertNotNull("String should not be null", str);
        assertTrue("Should contain value", str.indexOf(VALUE.toString()) >= 0);
    }
}
