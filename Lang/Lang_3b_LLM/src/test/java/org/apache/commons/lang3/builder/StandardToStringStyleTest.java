package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringStyleTest.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StandardToStringStyleTest {

    private final Integer base = Integer.valueOf(5);
    private final String baseStr = "Integer";

    private static final StandardToStringStyle STYLE = new StandardToStringStyle();

    static {
        STYLE.setUseShortClassName(true);
        STYLE.setUseIdentityHashCode(false);
        STYLE.setArrayStart("[");
        STYLE.setArraySeparator(", ");
        STYLE.setArrayEnd("]");
        STYLE.setNullText("%NULL%");
        STYLE.setSizeStartText("%SIZE=");
        STYLE.setSizeEndText("%");
        STYLE.setSummaryObjectStartText("%");
        STYLE.setSummaryObjectEndText("%");
    }

    @Before
    public void setUp() {
        ToStringBuilder.setDefaultStyle(STYLE);
    }

    @After
    public void tearDown() {
        ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
    }

    @Test
    public void testNullFieldOutput() {
        Person person = new Person(null, 0);
        String result = ToStringBuilder.reflectionToString(person);
        assertTrue("Should show null text", result.contains("%NULL%"));
    }

    @Test
    public void testArrayFormatting() {
        int[] arr = { 1, 2, 3 };
        String result = ToStringBuilder.reflectionToString(arr, STYLE);
        assertTrue("Should start with [", result.contains("["));
        assertTrue("Should contain separator", result.contains(", "));
        assertTrue("Should end with ]", result.contains("]"));
    }
    
    @Test
    public void testMapFormatting() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 1);
        map.put("b", 2);
        String result = ToStringBuilder.reflectionToString(map, STYLE);
        assertTrue("Should include key", result.contains("a"));
        assertTrue("Should include value", result.contains("2"));
    }

    @Test
    public void testSimpleObjectFormatting() {
        Person person = new Person("Bob", 25);
        String result = ToStringBuilder.reflectionToString(person, STYLE);
        assertTrue("Should include class short name", result.contains("Person"));
        assertTrue("Should contain name field", result.contains("name=Bob"));
    }
}
