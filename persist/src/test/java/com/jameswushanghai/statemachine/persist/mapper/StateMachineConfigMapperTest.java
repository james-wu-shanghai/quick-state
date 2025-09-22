package com.jameswushanghai.statemachine.persist.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;

import com.jameswushanghai.statemachine.persist.entity.StateMachineDBConfig;

/**
 * Unit test for StateMachineConfigMapper
 * This test verifies the structure and method signatures of the Mapper interface
 */
public class StateMachineConfigMapperTest {

    @Test
    public void testMapperAnnotation() {
        // Check if the interface has the @Mapper annotation
        Class<StateMachineConfigMapper> mapperClass = StateMachineConfigMapper.class;
        assertTrue(mapperClass.isAnnotationPresent(Mapper.class),
                "StateMachineConfigMapper should be annotated with @Mapper");
    }

    @Test
    public void testInterfaceStructure() {
        // Verify the interface structure
        Class<StateMachineConfigMapper> mapperClass = StateMachineConfigMapper.class;
        
        // Check that it's an interface
        assertTrue(mapperClass.isInterface(), "StateMachineConfigMapper should be an interface");
        
        // Check that it's a public interface
        assertTrue(java.lang.reflect.Modifier.isPublic(mapperClass.getModifiers()),
                "StateMachineConfigMapper should be a public interface");
    }

    @Test
    public void testMethodSignatures() throws NoSuchMethodException {
        // Verify that all expected methods are present with correct signatures
        Class<StateMachineConfigMapper> mapperClass = StateMachineConfigMapper.class;
        
        // Check selectById method
        Method selectByIdMethod = mapperClass.getMethod("selectById", Long.class);
        assertNotNull(selectByIdMethod, "selectById method should exist");
        assertEquals(StateMachineDBConfig.class, selectByIdMethod.getReturnType(),
                "selectById should return StateMachineDBConfig");
        
        // Check selectByName method
        Method selectByNameMethod = mapperClass.getMethod("selectByName", String.class);
        assertNotNull(selectByNameMethod, "selectByName method should exist");
        assertEquals(StateMachineDBConfig.class, selectByNameMethod.getReturnType(),
                "selectByName should return StateMachineDBConfig");
        
        // Check selectByNameAndVersion method
        Method selectByNameAndVersionMethod = mapperClass.getMethod("selectByNameAndVersion", String.class, Integer.class);
        assertNotNull(selectByNameAndVersionMethod, "selectByNameAndVersion method should exist");
        assertEquals(StateMachineDBConfig.class, selectByNameAndVersionMethod.getReturnType(),
                "selectByNameAndVersion should return StateMachineDBConfig");
        
        // Check selectAll method
        Method selectAllMethod = mapperClass.getMethod("selectAll");
        assertNotNull(selectAllMethod, "selectAll method should exist");
        assertEquals(List.class, selectAllMethod.getReturnType(),
                "selectAll should return List");
        
        // Check insert method
        Method insertMethod = mapperClass.getMethod("insert", StateMachineDBConfig.class);
        assertNotNull(insertMethod, "insert method should exist");
        assertEquals(int.class, insertMethod.getReturnType(),
                "insert should return int");
        
        // Check update method
        Method updateMethod = mapperClass.getMethod("update", StateMachineDBConfig.class);
        assertNotNull(updateMethod, "update method should exist");
        assertEquals(int.class, updateMethod.getReturnType(),
                "update should return int");
        
        // Check deleteById method
        Method deleteByIdMethod = mapperClass.getMethod("deleteById", Long.class);
        assertNotNull(deleteByIdMethod, "deleteById method should exist");
        assertEquals(int.class, deleteByIdMethod.getReturnType(),
                "deleteById should return int");
        
        // Check deleteByName method
        Method deleteByNameMethod = mapperClass.getMethod("deleteByName", String.class);
        assertNotNull(deleteByNameMethod, "deleteByName method should exist");
        assertEquals(int.class, deleteByNameMethod.getReturnType(),
                "deleteByName should return int");
        
        // Check existsByName method
        Method existsByNameMethod = mapperClass.getMethod("existsByName", String.class);
        assertNotNull(existsByNameMethod, "existsByName method should exist");
        assertEquals(int.class, existsByNameMethod.getReturnType(),
                "existsByName should return int");
        
        // Check getMaxVersionByName method
        Method getMaxVersionByNameMethod = mapperClass.getMethod("getMaxVersionByName", String.class);
        assertNotNull(getMaxVersionByNameMethod, "getMaxVersionByName method should exist");
        assertEquals(Integer.class, getMaxVersionByNameMethod.getReturnType(),
                "getMaxVersionByName should return Integer");
    }

    @Test
    public void testMethodCount() {
        // Verify the total number of methods in the interface
        Method[] methods = StateMachineConfigMapper.class.getDeclaredMethods();
        assertEquals(11, methods.length, "StateMachineConfigMapper should have exactly 11 methods");
    }
}