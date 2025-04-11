package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class CompareToBuilderTest {

    static class TestObject implements Comparable<TestObject> {
        private int a;
        public TestObject(final int a) {
            this.a = a;
        }
        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestObject)) return false;
            final TestObject rhs = (TestObject) o;
            return a == rhs.a;
        }
        @Override
        public int hashCode() {
            return a;
        }
        public void setA(final int a) {
            this.a = a;
        }
        public int getA() {
            return a;
        }
        @Override
        public int compareTo(final TestObject rhs) {
            return a < rhs.a ? -1 : a > rhs.a ? +1 : 0;
        }
    }

    static class TestSubObject extends TestObject {
        private int b;
        public TestSubObject() {
            super(0);
        }
        public TestSubObject(final int a, final int b) {
            super(a);
            this.b = b;
        }
        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestSubObject)) return false;
            final TestSubObject rhs = (TestSubObject) o;
            return super.equals(o) && b == rhs.b;
        }
    }

    static class TestTransientSubObject extends TestObject {
        private transient int t;
        public TestTransientSubObject(final int a, final int t) {
            super(a);
            this.t = t;
        }
    }

    @Test
    public void testBasicComparison() {
        assertEquals(0, new CompareToBuilder().append(1, 1).toComparison());
        assertTrue(new CompareToBuilder().append(1, 2).toComparison() < 0);
        assertTrue(new CompareToBuilder().append(2, 1).toComparison() > 0);
    }

    @Test
    public void testMultipleFieldsComparison() {
        assertEquals(0, new CompareToBuilder().append(1, 1).append(2, 2).toComparison());
        assertTrue(new CompareToBuilder().append(1, 1).append(2, 3).toComparison() < 0);
        assertTrue(new CompareToBuilder().append(1, 1).append(4, 2).toComparison() > 0);
    }

    @Test
    public void testObjectComparison() {
        assertEquals(0, new CompareToBuilder().append("abc", "abc").toComparison());
        assertTrue(new CompareToBuilder().append("abc", "def").toComparison() < 0);
        assertTrue(new CompareToBuilder().append("def", "abc").toComparison() > 0);
    }

    @Test
    public void testNullHandling() {
        assertTrue(new CompareToBuilder().append(null, "abc").toComparison() < 0);
        assertTrue(new CompareToBuilder().append("abc", null).toComparison() > 0);
        assertEquals(0, new CompareToBuilder().append((Object) null, (Object) null).toComparison());
    }

    @Test
    public void testBigIntegerComparison() {
        BigInteger b1 = new BigInteger("12345678901234567890");
        BigInteger b2 = new BigInteger("12345678901234567891");
        assertTrue(new CompareToBuilder().append(b1, b2).toComparison() < 0);
        assertTrue(new CompareToBuilder().append(b2, b1).toComparison() > 0);
    }

    @Test
    public void testCustomComparable() {
        TestObject a = new TestObject(5);
        TestObject b = new TestObject(10);
        assertTrue(new CompareToBuilder().append(a.getA(), b.getA()).toComparison() < 0);
        assertTrue(new CompareToBuilder().append(b.getA(), a.getA()).toComparison() > 0);
    }

    @Test
    public void testCompareToBuilderChainingStopsOnFirstDifference() {
        // Should stop after the first non-zero comparison
        CompareToBuilder builder = new CompareToBuilder().append(1, 2).append("abc", "abc"); // string shouldn't matter
        assertTrue(builder.toComparison() < 0);
    }

    @Test
    public void testEqualsSymmetry() {
        TestObject a = new TestObject(42);
        TestObject b = new TestObject(42);
        assertEquals(0, new CompareToBuilder().append(a.getA(), b.getA()).toComparison());
        assertEquals(0, new CompareToBuilder().append(b.getA(), a.getA()).toComparison());
    }
}