package com.jameswushanghai.statemachine.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Context的测试类
 */
public class ContextTest {

    private Context context;

    @Before
    public void setUp() {
        context = new Context();
    }

    @Test
    public void testSetAndGet() {
        // 测试设置和获取不同类型的值
        String key1 = "stringValue";
        String value1 = "testString";
        context.set(key1, value1);
        assertEquals(value1, context.get(key1));

        String key2 = "intValue";
        Integer value2 = 123;
        context.set(key2, value2);
        assertEquals(value2, context.get(key2));

        String key3 = "booleanValue";
        Boolean value3 = true;
        context.set(key3, value3);
        assertEquals(value3, context.get(key3));

        String key4 = "nullValue";
        context.set(key4, null);
        assertNull(context.get(key4));
    }

    @Test
    public void testGetWithType() {
        // 测试带类型的获取方法
        String key1 = "stringValue";
        String value1 = "testString";
        context.set(key1, value1);
        assertEquals(value1, context.get(key1, String.class));

        String key2 = "intValue";
        Integer value2 = 123;
        context.set(key2, value2);
        assertEquals(value2, context.get(key2, Integer.class));

        String key3 = "booleanValue";
        Boolean value3 = true;
        context.set(key3, value3);
        assertEquals(value3, context.get(key3, Boolean.class));

        // 测试类型转换
        String key4 = "stringNumber";
        String value4 = "456";
        context.set(key4, value4);
        assertThrows(ClassCastException.class, () -> {
            context.get(key4, Integer.class);
        });
    }

    @Test
    public void testGetOrDefault() {
        // 测试获取默认值
        String key1 = "existingKey";
        String value1 = "testValue";
        context.set(key1, value1);
        String defaultValue = "defaultValue";
        assertEquals(value1, context.getOrDefault(key1, defaultValue));

        String key2 = "nonExistingKey";
        assertEquals(defaultValue, context.getOrDefault(key2, defaultValue));

        // 测试空值的默认值
        String key3 = "nullKey";
        context.set(key3, null);
        assertEquals(defaultValue, context.getOrDefault(key3, defaultValue));
    }

    @Test
    public void testContains() {
        // 测试contains方法
        String key1 = "existingKey";
        context.set(key1, "testValue");
        assertTrue(context.contains(key1));

        String key2 = "nonExistingKey";
        assertFalse(context.contains(key2));

        // 测试contains对null值的处理
        String key3 = "nullKey";
        context.set(key3, null);
        assertTrue(context.contains(key3));
    }

    @Test
    public void testClear() {
        // 测试clear方法
        context.set("key1", "value1");
        context.set("key2", "value2");
        context.set("key3", "value3");

        assertFalse(context.isEmpty());
        context.clear();
        assertTrue(context.isEmpty());
        assertFalse(context.contains("key1"));
        assertFalse(context.contains("key2"));
        assertFalse(context.contains("key3"));
    }

    @Test
    public void testIsEmpty() {
        // 测试isEmpty方法
        assertTrue(context.isEmpty());
        context.set("key", "value");
        assertFalse(context.isEmpty());
        context.clear();
        assertTrue(context.isEmpty());
    }

    @Test
    public void testMultipleContextInstances() {
        // 测试多个Context实例之间的独立性
        Context context1 = new Context();
        Context context2 = new Context();

        context1.set("key", "value1");
        context2.set("key", "value2");

        assertEquals("value1", context1.get("key"));
        assertEquals("value2", context2.get("key"));
    }
}