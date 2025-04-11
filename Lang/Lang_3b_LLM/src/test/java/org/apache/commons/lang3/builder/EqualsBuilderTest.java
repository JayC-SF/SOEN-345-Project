package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class EqualsBuilderTest {

    static class TestObject {
        private final int a;
        public TestObject(int a) {
            this.a = a;
        }
        public int getA() {
            return a;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TestObject)) return false;
            TestObject other = (TestObject) obj;
            return new EqualsBuilder()
                    .append(this.a, other.a)
                    .isEquals();
        }
    }

    static class TestSubObject extends TestObject {
        private final int b;
        public TestSubObject(int a, int b) {
            super(a);
            this.b = b;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TestSubObject)) return false;
            TestSubObject other = (TestSubObject) obj;
            return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(this.b, other.b)
                    .isEquals();
        }
    }

    @Test
    public void testEqualsPrimitiveFields() {
        assertTrue(new EqualsBuilder().append(1, 1).isEquals());
        assertFalse(new EqualsBuilder().append(1, 2).isEquals());
    }

    @Test
    public void testEqualsObjects() {
        assertTrue(new EqualsBuilder().append("abc", "abc").isEquals());
        assertFalse(new EqualsBuilder().append("abc", "def").isEquals());
    }

    @Test
    public void testEqualsWithNulls() {
        assertTrue(new EqualsBuilder().append((Object) null, (Object) null).isEquals());
        assertFalse(new EqualsBuilder().append("abc", null).isEquals());
        assertFalse(new EqualsBuilder().append(null, "abc").isEquals());
    }

    @Test
    public void testEqualsArrays() {
        assertTrue(new EqualsBuilder().append(new int[]{1,2}, new int[]{1,2}).isEquals());
        assertFalse(new EqualsBuilder().append(new int[]{1,2}, new int[]{2,1}).isEquals());

        assertTrue(new EqualsBuilder().append(new Object[]{"a", "b"}, new Object[]{"a", "b"}).isEquals());
        assertFalse(new EqualsBuilder().append(new Object[]{"a", "b"}, new Object[]{"b", "a"}).isEquals());
    }

    @Test
    public void testEqualsCustomObjects() {
        TestObject obj1 = new TestObject(10);
        TestObject obj2 = new TestObject(10);
        TestObject obj3 = new TestObject(20);
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));
    }

    @Test
    public void testEqualsSubObjects() {
        TestSubObject s1 = new TestSubObject(1, 2);
        TestSubObject s2 = new TestSubObject(1, 2);
        TestSubObject s3 = new TestSubObject(1, 3);
        assertTrue(s1.equals(s2));
        assertFalse(s1.equals(s3));
    }

    @Test
    public void testAppendSuper() {
        assertTrue(new EqualsBuilder().appendSuper(true).append(1, 1).isEquals());
        assertFalse(new EqualsBuilder().appendSuper(false).append(1, 1).isEquals());
    }

    @Test
    public void testReflectionEquals() {
        TestObject obj1 = new TestObject(5);
        TestObject obj2 = new TestObject(5);
        TestObject obj3 = new TestObject(10);
        assertTrue(EqualsBuilder.reflectionEquals(obj1, obj2));
        assertFalse(EqualsBuilder.reflectionEquals(obj1, obj3));
    }

    @Test
    public void testReflectionEqualsWithNull() {
        TestObject obj1 = new TestObject(42);
        assertFalse(EqualsBuilder.reflectionEquals(obj1, null));
        assertFalse(EqualsBuilder.reflectionEquals(null, obj1));
        assertTrue(EqualsBuilder.reflectionEquals(null, null));
    }

    @Test
    public void testReflectionEqualsInvalidArgs() {
        assertFalse(EqualsBuilder.reflectionEquals("abc", 123));
    }
    
}