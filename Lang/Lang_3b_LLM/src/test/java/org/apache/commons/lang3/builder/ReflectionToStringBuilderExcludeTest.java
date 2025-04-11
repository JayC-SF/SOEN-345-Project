package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class ReflectionToStringBuilderExcludeTest {

    class TestFixture {
        @SuppressWarnings("unused")
        private final String secretField = SECRET_VALUE;

        @SuppressWarnings("unused")
        private final String showField = NOT_SECRET_VALUE;
    }

    private static final String NOT_SECRET_FIELD = "showField";
    private static final String NOT_SECRET_VALUE = "Hello World!";
    private static final String SECRET_FIELD = "secretField";
    private static final String SECRET_VALUE = "secret value";

    @Test
    public void testFieldExclusionByName() {
        TestFixture fixture = new TestFixture();
        String result = new ReflectionToStringBuilder(fixture)
                .setExcludeFieldNames(SECRET_FIELD)
                .toString();

        Assert.assertTrue("Should contain non-secret field", result.contains(NOT_SECRET_VALUE));
        Assert.assertFalse("Should not contain secret field", result.contains(SECRET_VALUE));
    }

    @Test
    public void testExcludeNullFieldName() {
        TestFixture fixture = new TestFixture();
        String result = new ReflectionToStringBuilder(fixture)
                .setExcludeFieldNames((String[]) null)
                .toString();

        Assert.assertTrue(result.contains(NOT_SECRET_VALUE));
        Assert.assertTrue(result.contains(SECRET_VALUE));
    }

    @Test
    public void testExcludeEmptyFieldArray() {
        TestFixture fixture = new TestFixture();
        String result = new ReflectionToStringBuilder(fixture)
                .setExcludeFieldNames(new String[0])
                .toString();

        Assert.assertTrue(result.contains(NOT_SECRET_VALUE));
        Assert.assertTrue(result.contains(SECRET_VALUE));
    }

    @Test
    public void testFieldNameArrayCopied() {
        TestFixture fixture = new TestFixture();
        String[] excludeFields = new String[] { SECRET_FIELD };
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(fixture);
        builder.setExcludeFieldNames(excludeFields);
        excludeFields[0] = null; // mutation after setting should not affect builder

        String result = builder.toString();
        Assert.assertFalse(result.contains(SECRET_VALUE));
    }
}