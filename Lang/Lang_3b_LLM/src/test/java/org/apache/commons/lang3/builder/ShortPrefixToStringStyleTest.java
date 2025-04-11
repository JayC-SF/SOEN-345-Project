package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ToStringStyle.SHORT_PREFIX_STYLE}.
 */
public class ShortPrefixToStringStyleTest {

    private static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    private static class Book {
        private String title;
        private String author;

        public Book(String title, String author) {
            this.title = title;
            this.author = author;
        }
    }

    private static class Wrapper {
        Object field;

        Wrapper(Object field) {
            this.field = field;
        }
    }

    @Before
    public void setUp() {
        ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @After
    public void tearDown() {
        ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
    }

    @Test
    public void testBasicObjectOutput() {
        Person person = new Person("Alice", 30);
        String result = ToStringBuilder.reflectionToString(person);
        System.out.println(result);
        assertTrue("Should contain class short name", result.contains("Person"));
        assertTrue("Should contain field name", result.contains("name="));
        assertTrue("Should contain value", result.contains("Alice"));
    }

    @Test
    public void testNullFieldOutput() {
        Book book = new Book(null, "Orwell");
        String result = ToStringBuilder.reflectionToString(book);
        System.out.println(result);
        assertTrue("Should show null field", result.contains("title=<null>"));
        assertTrue("Should show non-null field", result.contains("author=Orwell"));
    }

    @Test
    public void testCollectionOutput() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("hello");
        list.add("world");

        Wrapper wrapped = new Wrapper(list);
        String result = ToStringBuilder.reflectionToString(wrapped);
        System.out.println(result);
        assertTrue("Should contain values", result.contains("hello"));
        assertTrue("Should contain values", result.contains("world"));
    }

    @Test
    public void testMapOutput() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("one", 1);
        map.put("two", 2);

        Wrapper wrapped = new Wrapper(map);
        String result = ToStringBuilder.reflectionToString(wrapped);
        System.out.println(result);
        assertTrue("Should contain key 'one'", result.contains("one"));
        assertTrue("Should contain key 'two'", result.contains("two"));
    }
}
