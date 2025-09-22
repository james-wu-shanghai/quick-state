package com.jameswushanghai.statemachine.persist.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit test for StateMachineDBConfig
 */
public class StateMachineDBConfigTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        StateMachineDBConfig config = new StateMachineDBConfig();
        Long id = 1L;
        String name = "testMachine";
        String configXml = "<stateMachine>...</stateMachine>";
        Integer version = 3;
        String description = "Test state machine configuration";
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime updateTime = LocalDateTime.now();

        // Act - Set all properties
        config.setId(id);
        config.setName(name);
        config.setConfigXml(configXml);
        config.setVersion(version);
        config.setDescription(description);
        config.setCreateTime(createTime);
        config.setUpdateTime(updateTime);

        // Assert - Verify all getters
        assertEquals(id, config.getId());
        assertEquals(name, config.getName());
        assertEquals(configXml, config.getConfigXml());
        assertEquals(version, config.getVersion());
        assertEquals(description, config.getDescription());
        assertEquals(createTime, config.getCreateTime());
        assertEquals(updateTime, config.getUpdateTime());
    }

    @Test
    public void testEqualsMethod() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        StateMachineDBConfig config1 = new StateMachineDBConfig();
        config1.setId(1L);
        config1.setName("testMachine");
        config1.setConfigXml("<stateMachine>...</stateMachine>");
        config1.setVersion(3);
        config1.setDescription("Test state machine configuration");
        config1.setCreateTime(now);
        config1.setUpdateTime(now);
        
        // Same values as config1
        StateMachineDBConfig config2 = new StateMachineDBConfig();
        config2.setId(1L);
        config2.setName("testMachine");
        config2.setConfigXml("<stateMachine>...</stateMachine>");
        config2.setVersion(3);
        config2.setDescription("Test state machine configuration");
        config2.setCreateTime(now);
        config2.setUpdateTime(now);
        
        // Different values from config1
        StateMachineDBConfig config3 = new StateMachineDBConfig();
        config3.setId(2L);
        config3.setName("differentMachine");
        config3.setConfigXml("<stateMachine>different...</stateMachine>");
        config3.setVersion(4);
        config3.setDescription("Different configuration");
        config3.setCreateTime(now.plusHours(1));
        config3.setUpdateTime(now.plusHours(1));
        
        // Act & Assert
        assertTrue(config1.equals(config1), "An object should be equal to itself");
        assertFalse(config1.equals(null), "An object should not be equal to null");
        assertFalse(config1.equals(new Object()), "An object should not be equal to an object of a different class");
        assertTrue(config1.equals(config2), "Objects with the same field values should be equal");
        assertFalse(config1.equals(config3), "Objects with different field values should not be equal");
    }
    
    @Test
    public void testHashCodeMethod() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        StateMachineDBConfig config1 = new StateMachineDBConfig();
        config1.setId(1L);
        config1.setName("testMachine");
        config1.setConfigXml("<stateMachine>...</stateMachine>");
        config1.setVersion(3);
        config1.setDescription("Test state machine configuration");
        config1.setCreateTime(now);
        config1.setUpdateTime(now);
        
        // Same values as config1
        StateMachineDBConfig config2 = new StateMachineDBConfig();
        config2.setId(1L);
        config2.setName("testMachine");
        config2.setConfigXml("<stateMachine>...</stateMachine>");
        config2.setVersion(3);
        config2.setDescription("Test state machine configuration");
        config2.setCreateTime(now);
        config2.setUpdateTime(now);
        
        // Different values from config1
        StateMachineDBConfig config3 = new StateMachineDBConfig();
        config3.setId(2L);
        config3.setName("differentMachine");
        
        // Act
        int hashCode1 = config1.hashCode();
        int hashCode2 = config2.hashCode();
        int hashCode3 = config3.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2, "Equal objects should have the same hash code");
        assertNotEquals(hashCode1, hashCode3, "Different objects may have different hash codes");
    }
    
    @Test
    public void testToStringMethod() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        StateMachineDBConfig config = new StateMachineDBConfig();
        config.setId(1L);
        config.setName("testMachine");
        config.setConfigXml("<stateMachine>test configuration content</stateMachine>");
        config.setVersion(3);
        config.setDescription("Test state machine configuration");
        config.setCreateTime(now);
        config.setUpdateTime(now);
        
        // Act
        String toStringResult = config.toString();
        
        // Assert
        assertNotNull(toStringResult, "toString() should not return null");
        assertFalse(toStringResult.isEmpty(), "toString() should not return an empty string");
        assertTrue(toStringResult.contains("StateMachineConfig"), "toString() should contain the class name");
        assertTrue(toStringResult.contains("testMachine"), "toString() should contain the name field");
        assertTrue(toStringResult.contains("test configuration content"), "toString() should contain part of the configXml");
    }
    
    @Test
    public void testToStringWithNullFields() {
        // Arrange
        StateMachineDBConfig config = new StateMachineDBConfig();
        
        // Act
        String toStringResult = config.toString();
        
        // Assert
        assertNotNull(toStringResult, "toString() should not return null even with null fields");
        assertTrue(toStringResult.contains("null"), "toString() should handle null fields correctly");
    }
    
    @Test
    public void testEqualsWithNullFields() {
        // Arrange
        StateMachineDBConfig config1 = new StateMachineDBConfig();
        
        StateMachineDBConfig config2 = new StateMachineDBConfig();
        
        // Act & Assert
        assertTrue(config1.equals(config2), "Objects with all null fields should be equal");
        
        // Set one field in config1
        config1.setName("testMachine");
        assertFalse(config1.equals(config2), "Objects with different null/non-null fields should not be equal");
    }
    
    @Test
    public void testHashCodeConsistency() {
        // Arrange
        StateMachineDBConfig config = new StateMachineDBConfig();
        config.setName("testMachine");
        
        // Act - Get hash code multiple times
        int hashCode1 = config.hashCode();
        int hashCode2 = config.hashCode();
        int hashCode3 = config.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent");
        assertEquals(hashCode1, hashCode3, "Hash code should be consistent");
    }
    
    @Test
    public void testToStringWithLongConfigXml() {
        // Arrange
        StateMachineDBConfig config = new StateMachineDBConfig();
        // Create a very long XML string
        StringBuilder longXmlBuilder = new StringBuilder("<stateMachine>");
        for (int i = 0; i < 100; i++) {
            longXmlBuilder.append("<state>state").append(i).append("</state>");
        }
        longXmlBuilder.append("</stateMachine>");
        String longXml = longXmlBuilder.toString();
        
        config.setConfigXml(longXml);
        
        // Act
        String toStringResult = config.toString();
        
        // Assert
        assertNotNull(toStringResult, "toString() should not return null with long configXml");
        assertTrue(toStringResult.contains("..."), "toString() should truncate long configXml with ellipsis");
        assertFalse(toStringResult.length() > longXml.length(), "toString() result should be shorter than the original configXml");
    }

    @Test
    public void testDefaultConstructor() {
        // Act
        StateMachineDBConfig config = new StateMachineDBConfig();

        // Assert
        assertNotNull(config);
    }

    @Test
    public void testSetAndGetWithNullValues() {
        // Arrange
        StateMachineDBConfig config = new StateMachineDBConfig();

        // Act
        config.setId(null);
        config.setName(null);
        config.setConfigXml(null);
        config.setVersion(null);
        config.setDescription(null);
        config.setCreateTime(null);
        config.setUpdateTime(null);

        // Assert
        assertEquals(null, config.getId());
        assertEquals(null, config.getName());
        assertEquals(null, config.getConfigXml());
        assertEquals(null, config.getVersion());
        assertEquals(null, config.getDescription());
        assertEquals(null, config.getCreateTime());
        assertEquals(null, config.getUpdateTime());
    }

    @Test
    public void testEqualityForPrimitiveTypes() {
        // Arrange
        StateMachineDBConfig config1 = new StateMachineDBConfig();
        StateMachineDBConfig config2 = new StateMachineDBConfig();
        Long id = 1L;
        Integer version = 2;

        // Act
        config1.setId(id);
        config1.setVersion(version);
        config2.setId(id);
        config2.setVersion(version);

        // Assert
        assertEquals(config1.getId(), config2.getId());
        assertEquals(config1.getVersion(), config2.getVersion());
    }

    @Test
    public void testStringPropertyLengths() {
        // Arrange
        StateMachineDBConfig config = new StateMachineDBConfig();
        String longString = "a".repeat(1000); // Create a long string

        // Act
        config.setName(longString);
        config.setConfigXml(longString);
        config.setDescription(longString);

        // Assert
        assertEquals(longString, config.getName());
        assertEquals(longString, config.getConfigXml());
        assertEquals(longString, config.getDescription());
    }
}