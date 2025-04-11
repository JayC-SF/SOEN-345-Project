package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.Objects;

public class HashCodeBuilderTest {

    static class Simple {
        private final int x;
        private final String y;

        public Simple(int x, String y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                .append(x)
                .append(y)
                .toHashCode();
        }
    }

    static class WithArray {
        private final int[] values;

        public WithArray(int[] values) {
            this.values = values;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                .append(values)
                .toHashCode();
        }
    }

    static class ReflectionTestCycleA {
        ReflectionTestCycleB b;

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }
    }

    static class ReflectionTestCycleB {
        ReflectionTestCycleA a;

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }
    }

    @Test
    public void testHashCodeBasic() {
        Simple obj1 = new Simple(5, "foo");
        Simple obj2 = new Simple(5, "foo");
        Simple obj3 = new Simple(6, "bar");

        assertEquals(obj1.hashCode(), obj2.hashCode());
        assertNotEquals(obj1.hashCode(), obj3.hashCode());
    }

    @Test
    public void testHashCodeWithArray() {
        WithArray arr1 = new WithArray(new int[]{1, 2, 3});
        WithArray arr2 = new WithArray(new int[]{1, 2, 3});
        WithArray arr3 = new WithArray(new int[]{3, 2, 1});

        assertEquals(arr1.hashCode(), arr2.hashCode());
        assertNotEquals(arr1.hashCode(), arr3.hashCode());
    }

    @Test
    public void testReflectionHashCode() {
        Simple obj1 = new Simple(42, "abc");
        Simple obj2 = new Simple(42, "abc");
        assertEquals(
            HashCodeBuilder.reflectionHashCode(obj1),
            HashCodeBuilder.reflectionHashCode(obj2)
        );
    }

    @Test
    public void testReflectionHashCodeHandlesCycles() {
        ReflectionTestCycleA a = new ReflectionTestCycleA();
        ReflectionTestCycleB b = new ReflectionTestCycleB();
        a.b = b;
        b.a = a;
        // Should not stack overflow
        int hash = a.hashCode();
        assertTrue("Hash should be non-zero", hash != 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialNonZeroOddNumber() {
        new HashCodeBuilder(0, 37);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMultiplierNonZeroOddNumber() {
        new HashCodeBuilder(17, 0);
    }
}