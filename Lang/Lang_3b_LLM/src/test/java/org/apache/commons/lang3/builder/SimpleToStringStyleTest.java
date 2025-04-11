package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ToStringStyle.SIMPLE_STYLE}.
 */
public class SimpleToStringStyleTest {

    private static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    private static class Wrapper {
        Object content;

        public Wrapper(Object content) {
            this.content = content;
        }
    }

    @Before
    public void setUp() throws Exception {
        ToStringBuilder.setDefaultStyle(ToStringStyle.SIMPLE_STYLE);
    }

    @After
    public void tearDown() throws Exception {
        ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
    }

    @Test
    public void testPersonToString() {
        Person p = new Person("Alice", 30);
        String result = ToStringBuilder.reflectionToString(p);
        System.out.println(result);
        assertTrue("Should include name", result.contains("Alice"));
        assertTrue("Should include age", result.contains("30"));
        assertTrue("Should not include class name", !result.contains("Person"));
    }

    @Test
    public void testCollectionOutput() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("red");
        list.add("blue");
        Wrapper wrapped = new Wrapper(list);
        String result = ToStringBuilder.reflectionToString(wrapped);
        System.out.println(result);
        assertTrue(result.contains("red"));
        assertTrue(result.contains("blue"));
    }

    @Test
    public void testMapOutput() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        Wrapper wrapped = new Wrapper(map);
        String result = ToStringBuilder.reflectionToString(wrapped);
        System.out.println(result);
        assertTrue(result.contains("key1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    public void testNullFields() {
        Person p = new Person(null, 25);
        String result = ToStringBuilder.reflectionToString(p);
        System.out.println(result);
        assertTrue(result.contains("<null>"));
        assertTrue(result.contains("25"));
    }
}
