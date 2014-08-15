package com.shnaper.properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Test cases
 * @author Rostislav Shnaper
 * 2014-08-14
 */
public class PropertyResolverTest {
    private Properties properties;

    @Before
    public void setUp() throws Exception {
        properties = new EnhancedProperties();
    }

    @After
    public void tearDown() throws Exception {
        properties.clear();
    }

    @Test
    public void testSimpleResolve() {
        properties.put("message","This is ${value}");
        properties.put("value","a test");

        String value = properties.getProperty("message");
        assertEquals(value,"This is a test");
    }

    @Test
    public void testDotResolve() {
        properties.put("message","This is ${some.value}");
        properties.put("some.value","a test");


        String value = properties.getProperty("message");
        assertEquals(value,"This is a test");
    }

    @Test
    public void testDashResolve() {
        properties.put("message","This is ${some-value}");
        properties.put("some-value","a test");

        String value = properties.getProperty("message");
        assertEquals(value,"This is a test");
    }

    @Test
    public void testNonResolve() {
        properties.put("message","This is ${some-non-existing-value}");
        properties.put("some.value","a value");


        String value = properties.getProperty("message");
        assertEquals(value,"This is ${some-non-existing-value}");
    }

    @Test
    public void testMultiResolve() {
        properties.put("message","This is ${value1} ${value3}");
        properties.put("value1","some value that ${value2} on another");
        properties.put("value2","depends");
        properties.put("value3","value");


        String value = properties.getProperty("message");
        assertEquals(value,"This is some value that depends on another value");
    }

    @Test(expected = RuntimeException.class)
    public void testCycleResolve() {
        properties.put("message","This is ${value1}");
        properties.put("value1","a value that ${value2}");
        properties.put("value2","depends on ${value3}");
        properties.put("value3","${value1}");


        properties.getProperty("message");
        fail("We are expecting an exception at this point");
    }
}