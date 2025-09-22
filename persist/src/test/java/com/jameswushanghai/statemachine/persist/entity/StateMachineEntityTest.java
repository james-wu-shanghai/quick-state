package com.jameswushanghai.statemachine.persist.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit test for StateMachineEntity
 */
public class StateMachineEntityTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        StateMachineEntity entity = new StateMachineEntity();
        Long id = 1L;
        String name = "testMachine";
        String configName = "testConfig";
        Integer configVersion = 2;
        String currentState = "STATE1";
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime updateTime = LocalDateTime.now();

        // Act - Set all properties
        entity.setId(id);
        entity.setName(name);
        entity.setConfigName(configName);
        entity.setConfigVersion(configVersion);
        entity.setCurrentState(currentState);
        entity.setCreateTime(createTime);
        entity.setUpdateTime(updateTime);

        // Assert - Verify all getters
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(configName, entity.getConfigName());
        assertEquals(configVersion, entity.getConfigVersion());
        assertEquals(currentState, entity.getCurrentState());
        assertEquals(createTime, entity.getCreateTime());
        assertEquals(updateTime, entity.getUpdateTime());
    }

    @Test
    public void testEqualsMethod() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        StateMachineEntity entity1 = new StateMachineEntity();
        entity1.setId(1L);
        entity1.setName("testMachine");
        entity1.setConfigName("testConfig");
        entity1.setConfigVersion(2);
        entity1.setCurrentState("STATE1");
        entity1.setCreateTime(now);
        entity1.setUpdateTime(now);
        
        // Same values as entity1
        StateMachineEntity entity2 = new StateMachineEntity();
        entity2.setId(1L);
        entity2.setName("testMachine");
        entity2.setConfigName("testConfig");
        entity2.setConfigVersion(2);
        entity2.setCurrentState("STATE1");
        entity2.setCreateTime(now);
        entity2.setUpdateTime(now);
        
        // Different values from entity1
        StateMachineEntity entity3 = new StateMachineEntity();
        entity3.setId(2L);
        entity3.setName("differentMachine");
        entity3.setConfigName("differentConfig");
        entity3.setConfigVersion(3);
        entity3.setCurrentState("STATE2");
        entity3.setCreateTime(now.plusHours(1));
        entity3.setUpdateTime(now.plusHours(1));
        
        // Act & Assert
        assertTrue(entity1.equals(entity1), "An object should be equal to itself");
        assertFalse(entity1.equals(null), "An object should not be equal to null");
        assertFalse(entity1.equals(new Object()), "An object should not be equal to an object of a different class");
        assertTrue(entity1.equals(entity2), "Objects with the same field values should be equal");
        assertFalse(entity1.equals(entity3), "Objects with different field values should not be equal");
    }
    
    @Test
    public void testHashCodeMethod() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        StateMachineEntity entity1 = new StateMachineEntity();
        entity1.setId(1L);
        entity1.setName("testMachine");
        entity1.setConfigName("testConfig");
        entity1.setConfigVersion(2);
        entity1.setCurrentState("STATE1");
        entity1.setCreateTime(now);
        entity1.setUpdateTime(now);
        
        // Same values as entity1
        StateMachineEntity entity2 = new StateMachineEntity();
        entity2.setId(1L);
        entity2.setName("testMachine");
        entity2.setConfigName("testConfig");
        entity2.setConfigVersion(2);
        entity2.setCurrentState("STATE1");
        entity2.setCreateTime(now);
        entity2.setUpdateTime(now);
        
        // Different values from entity1
        StateMachineEntity entity3 = new StateMachineEntity();
        entity3.setId(2L);
        entity3.setName("differentMachine");
        
        // Act
        int hashCode1 = entity1.hashCode();
        int hashCode2 = entity2.hashCode();
        int hashCode3 = entity3.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2, "Equal objects should have the same hash code");
        assertNotEquals(hashCode1, hashCode3, "Different objects may have different hash codes");
    }
    
    @Test
    public void testToStringMethod() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        StateMachineEntity entity = new StateMachineEntity();
        entity.setId(1L);
        entity.setName("testMachine");
        entity.setConfigName("testConfig");
        entity.setConfigVersion(2);
        entity.setCurrentState("STATE1");
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        
        // Act
        String toStringResult = entity.toString();
        
        // Assert
        assertNotNull(toStringResult, "toString() should not return null");
        assertFalse(toStringResult.isEmpty(), "toString() should not return an empty string");
        assertTrue(toStringResult.contains("StateMachineEntity"), "toString() should contain the class name");
        assertTrue(toStringResult.contains("testMachine"), "toString() should contain the name field");
        assertTrue(toStringResult.contains("STATE1"), "toString() should contain the currentState field");
    }
    
    @Test
    public void testEqualsWithNullFields() {
        // Arrange
        StateMachineEntity entity1 = new StateMachineEntity();
        
        StateMachineEntity entity2 = new StateMachineEntity();
        
        // Act & Assert
        assertTrue(entity1.equals(entity2), "Objects with all null fields should be equal");
        
        // Set one field in entity1
        entity1.setName("testMachine");
        assertFalse(entity1.equals(entity2), "Objects with different null/non-null fields should not be equal");
    }
    
    @Test
    public void testHashCodeConsistency() {
        // Arrange
        StateMachineEntity entity = new StateMachineEntity();
        entity.setName("testMachine");
        
        // Act - Get hash code multiple times
        int hashCode1 = entity.hashCode();
        int hashCode2 = entity.hashCode();
        int hashCode3 = entity.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent");
        assertEquals(hashCode1, hashCode3, "Hash code should be consistent");
    }

    @Test
    public void testDefaultConstructor() {
        // Act
        StateMachineEntity entity = new StateMachineEntity();

        // Assert
        assertNotNull(entity);
    }

    @Test
    public void testSetAndGetWithNullValues() {
        // Arrange
        StateMachineEntity entity = new StateMachineEntity();

        // Act
        entity.setId(null);
        entity.setName(null);
        entity.setConfigName(null);
        entity.setConfigVersion(null);
        entity.setCurrentState(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(null);

        // Assert
        assertEquals(null, entity.getId());
        assertEquals(null, entity.getName());
        assertEquals(null, entity.getConfigName());
        assertEquals(null, entity.getConfigVersion());
        assertEquals(null, entity.getCurrentState());
        assertEquals(null, entity.getCreateTime());
        assertEquals(null, entity.getUpdateTime());
    }

    @Test
    public void testEqualityForPrimitiveTypes() {
        // Arrange
        StateMachineEntity entity1 = new StateMachineEntity();
        StateMachineEntity entity2 = new StateMachineEntity();
        Long id = 1L;
        Integer version = 2;

        // Act
        entity1.setId(id);
        entity1.setConfigVersion(version);
        entity2.setId(id);
        entity2.setConfigVersion(version);

        // Assert
        assertEquals(entity1.getId(), entity2.getId());
        assertEquals(entity1.getConfigVersion(), entity2.getConfigVersion());
    }

    @Test
    public void testStringPropertyLengths() {
        // Arrange
        StateMachineEntity entity = new StateMachineEntity();
        String longString = "a".repeat(1000); // Create a long string

        // Act
        entity.setName(longString);
        entity.setConfigName(longString);
        entity.setCurrentState(longString);

        // Assert
        assertEquals(longString, entity.getName());
        assertEquals(longString, entity.getConfigName());
        assertEquals(longString, entity.getCurrentState());
    }
}