package com.jameswushanghai.statemachine.persist.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;

import com.jameswushanghai.statemachine.persist.entity.StateMachineEntity;

/**
 * Unit test for StateMachineMapper
 * This test verifies the structure and method signatures of the Mapper interface
 */
public class StateMachineMapperTest {

    @Test
    public void testMapperAnnotation() {
        // Check if the interface has the @Mapper annotation
        Class<StateMachineMapper> mapperClass = StateMachineMapper.class;
        assertTrue(mapperClass.isAnnotationPresent(Mapper.class),
                "StateMachineMapper should be annotated with @Mapper");
    }

    @Test
    public void testInterfaceStructure() {
        // Verify the interface structure
        Class<StateMachineMapper> mapperClass = StateMachineMapper.class;
        
        // Check that it's an interface
        assertTrue(mapperClass.isInterface(), "StateMachineMapper should be an interface");
        
        // Check that it's a public interface
        assertTrue(java.lang.reflect.Modifier.isPublic(mapperClass.getModifiers()),
                "StateMachineMapper should be a public interface");
    }

    @Test
    public void testMethodSignatures() throws NoSuchMethodException {
        // Verify that all expected methods are present with correct signatures
        Class<StateMachineMapper> mapperClass = StateMachineMapper.class;
        
        // Check selectById method
        Method selectByIdMethod = mapperClass.getMethod("selectById", Long.class);
        assertNotNull(selectByIdMethod, "selectById method should exist");
        assertEquals(StateMachineEntity.class, selectByIdMethod.getReturnType(),
                "selectById should return StateMachineEntity");
        
        // Check selectByName method
        Method selectByNameMethod = mapperClass.getMethod("selectByName", String.class);
        assertNotNull(selectByNameMethod, "selectByName method should exist");
        assertEquals(StateMachineEntity.class, selectByNameMethod.getReturnType(),
                "selectByName should return StateMachineEntity");
        
        // Check selectByConfigName method
        Method selectByConfigNameMethod = mapperClass.getMethod("selectByConfigName", String.class);
        assertNotNull(selectByConfigNameMethod, "selectByConfigName method should exist");
        assertEquals(List.class, selectByConfigNameMethod.getReturnType(),
                "selectByConfigName should return List");
        
        // Check selectAll method
        Method selectAllMethod = mapperClass.getMethod("selectAll");
        assertNotNull(selectAllMethod, "selectAll method should exist");
        assertEquals(List.class, selectAllMethod.getReturnType(),
                "selectAll should return List");
        
        // Check insert method
        Method insertMethod = mapperClass.getMethod("insert", StateMachineEntity.class);
        assertNotNull(insertMethod, "insert method should exist");
        assertEquals(int.class, insertMethod.getReturnType(),
                "insert should return int");
        
        // Check update method
        Method updateMethod = mapperClass.getMethod("update", StateMachineEntity.class);
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
        
        // Check deleteByConfigName method
        Method deleteByConfigNameMethod = mapperClass.getMethod("deleteByConfigName", String.class);
        assertNotNull(deleteByConfigNameMethod, "deleteByConfigName method should exist");
        assertEquals(int.class, deleteByConfigNameMethod.getReturnType(),
                "deleteByConfigName should return int");
        
        // Check existsByName method
        Method existsByNameMethod = mapperClass.getMethod("existsByName", String.class);
        assertNotNull(existsByNameMethod, "existsByName method should exist");
        assertEquals(int.class, existsByNameMethod.getReturnType(),
                "existsByName should return int");
    }

    @Test
    public void testMethodCount() {
        // Verify the total number of methods in the interface
        Method[] methods = StateMachineMapper.class.getDeclaredMethods();
        assertEquals(10, methods.length, "StateMachineMapper should have exactly 10 methods");
    }
}