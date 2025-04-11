package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test case for ToStringStyle.
 */
public class ToStringStyleTest {

    static class Person {
        private final String name;
        private final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    private static class ToStringStyleImpl extends ToStringStyle {
        private static final long serialVersionUID = 1L;
    }

    @Test
    public void testSetNullText() {
        ToStringStyleImpl style = new ToStringStyleImpl();
        style.setNullText("<null>");
        assertEquals("<null>", style.getNullText());
    }

    @Test
    public void testSetFieldSeparator() {
        ToStringStyleImpl style = new ToStringStyleImpl();
        style.setFieldSeparator(" | ");
        assertEquals(" | ", style.getFieldSeparator());
    }

    @Test
    public void testSetUseClassName() {
        ToStringStyleImpl style = new ToStringStyleImpl();
        style.setUseClassName(false);
        assertEquals(false, style.isUseClassName());
    }

    @Test
    public void testSetUseIdentityHashCode() {
        ToStringStyleImpl style = new ToStringStyleImpl();
        style.setUseIdentityHashCode(false);
        assertEquals(false, style.isUseIdentityHashCode());
    }

    @Test
    public void testSetUseFieldNames() {
        ToStringStyleImpl style = new ToStringStyleImpl();
        style.setUseFieldNames(false);
        assertEquals(false, style.isUseFieldNames());
    }

    @Test
    public void testSetContentStart() {
        ToStringStyleImpl style = new ToStringStyleImpl();
        style.setContentStart("<");
        assertEquals("<", style.getContentStart());
    }

    @Test
    public void testSetContentEnd() {
        ToStringStyleImpl style = new ToStringStyleImpl();
        style.setContentEnd(">");
        assertEquals(">", style.getContentEnd());
    }

    @Test
    public void testSetFieldNameValueSeparator() {
        ToStringStyleImpl style = new ToStringStyleImpl();
        style.setFieldNameValueSeparator(" = ");
        assertEquals(" = ", style.getFieldNameValueSeparator());
    }
} 
