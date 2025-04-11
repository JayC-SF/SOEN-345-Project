package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.builder.ToStringBuilder}.
 */
public class ToStringBuilderTest {

    private final Integer base = Integer.valueOf(5);
    private final String baseStr = base.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(base));


    @Test
    public void testAppendBasicFields() {
        ToStringBuilder builder = new ToStringBuilder(base);
        builder.append("a", "A").append("b", "B");
        String result = builder.toString();
        assertEquals(baseStr + "[a=A,b=B]", result);
    }

    @Test
    public void testAppendNull() {
        ToStringBuilder builder = new ToStringBuilder(base);
        builder.append("nullField", (Object) null);
        String result = builder.toString();
        assertEquals(baseStr + "[nullField=<null>]", result);
    }

    @Test
    public void testAppendList() {
        List<String> list = new ArrayList<String>();
        list.add("x");
        list.add("y");
        ToStringBuilder builder = new ToStringBuilder(base);
        builder.append("list", list);
        String result = builder.toString();
        assertEquals(baseStr + "[list=" + list.toString() + "]", result);
    }

    @Test
    public void testAppendMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("k1", 1);
        map.put("k2", 2);
        ToStringBuilder builder = new ToStringBuilder(base);
        builder.append("map", map);
        String result = builder.toString();
        assertEquals(baseStr + "[map=" + map.toString() + "]", result);
    }

    @Test
    public void testSetDefaultStyle() {
        ToStringStyle original = ToStringBuilder.getDefaultStyle();
        try {
            ToStringStyle newStyle = ToStringStyle.SHORT_PREFIX_STYLE;
            ToStringBuilder.setDefaultStyle(newStyle);
            assertSame(newStyle, ToStringBuilder.getDefaultStyle());
        } finally {
            ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
        }
    }

    @Test
    public void testReflectionToString() {
        DummyObject obj = new DummyObject("sample", 123);
        String result = ToStringBuilder.reflectionToString(obj);
        assertTrue("Should include field name", result.contains("name=sample"));
        assertTrue("Should include field value", result.contains("id=123"));
    }


    private static class DummyObject {
        private final String name;
        private final int id;

        public DummyObject(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }
}
