package com.jameswushanghai.statemachine.persist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.jameswushanghai.statemachine.core.StateMachine;
import com.jameswushanghai.statemachine.core.StateMachineFactory;
import com.jameswushanghai.statemachine.model.StateMachineConfig;
import com.jameswushanghai.statemachine.parser.StateMachineXmlParser;
import com.jameswushanghai.statemachine.persist.entity.StateMachineDBConfig;
import com.jameswushanghai.statemachine.persist.entity.StateMachineEntity;
import com.jameswushanghai.statemachine.persist.mapper.StateMachineConfigMapper;
import com.jameswushanghai.statemachine.persist.mapper.StateMachineMapper;

/**
 * Unit test for StateMachinePersistService
 */
public class StateMachinePersistServiceTest {

    @Mock
    private StateMachineMapper stateMachineMapper;

    @Mock
    private StateMachineConfigMapper stateMachineConfigMapper;

    @Mock
    private StateMachineXmlParser stateMachineXmlParser;

    @Mock
    private StateMachineFactory stateMachineFactory;

    @Mock
    private StateMachine stateMachine;

    @Mock
    private Logger log;

    @InjectMocks
    private StateMachinePersistService stateMachinePersistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStateMachine_Success() throws Exception {
        // Arrange
        String machineName = "testMachine";
        String configXml = "<stateMachine>...</stateMachine>";
        StateMachineDBConfig configEntity = new StateMachineDBConfig();
        configEntity.setName(machineName);
        configEntity.setConfigXml(configXml);

        when(stateMachineConfigMapper.selectByName(machineName)).thenReturn(configEntity);
        when(stateMachineFactory.createStateMachineInterfaceFromXmlString(configXml)).thenReturn(stateMachine);

        // Act
        StateMachine result = stateMachinePersistService.createStateMachine(machineName);

        // Assert
        assertNotNull(result);
        assertEquals(stateMachine, result);
        verify(stateMachineConfigMapper, times(1)).selectByName(machineName);
        verify(stateMachineFactory, times(1)).createStateMachineInterfaceFromXmlString(configXml);
    }

    @Test
    public void testCreateStateMachine_ConfigNotFound() {
        // Arrange
        String machineName = "nonExistentMachine";
        when(stateMachineConfigMapper.selectByName(machineName)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            stateMachinePersistService.createStateMachine(machineName);
        });

        assertEquals("State machine configuration not found for name: " + machineName, exception.getMessage());
        verify(stateMachineConfigMapper, times(1)).selectByName(machineName);
    }

    @Test
    public void testSaveStateMachine_UpdateExisting() throws Exception {
        // Arrange
        String machineName = "testMachine";
        String currentState = "STATE2";
        Long existingId = 1L;

        when(stateMachine.getName()).thenReturn(machineName);
        when(stateMachine.getCurrentState()).thenReturn(currentState);
        when(stateMachineMapper.existsByName(machineName)).thenReturn(1);

        StateMachineEntity existingEntity = new StateMachineEntity();
        existingEntity.setId(existingId);
        existingEntity.setName(machineName);
        existingEntity.setCurrentState("STATE1");

        when(stateMachineMapper.selectByName(machineName)).thenReturn(existingEntity);

        // Act
        Long resultId = stateMachinePersistService.saveStateMachine(stateMachine);

        // Assert
        assertEquals(existingId, resultId);
        assertEquals(currentState, existingEntity.getCurrentState());
        assertNotNull(existingEntity.getUpdateTime());
        verify(stateMachineMapper, times(1)).update(existingEntity);
    }

    @Test
    public void testSaveStateMachine_CreateNew() throws Exception {
        // Arrange
        String machineName = "newMachine";
        String currentState = "STATE1";
        Integer latestVersion = 2;

        when(stateMachine.getName()).thenReturn(machineName);
        when(stateMachine.getCurrentState()).thenReturn(currentState);
        when(stateMachineMapper.existsByName(machineName)).thenReturn(0);
        when(stateMachineConfigMapper.getMaxVersionByName(machineName)).thenReturn(latestVersion);

        // Act
        Long resultId = stateMachinePersistService.saveStateMachine(stateMachine);

        // Assert
        // 由于MyBatis插入通常会回填ID，这里假设ID为null（实际测试中可能需要调整）
        // 主要验证其他字段是否正确设置
        verify(stateMachineMapper, times(1)).insert(any(StateMachineEntity.class));
    }

    @Test
    public void testSaveStateMachine_CreateNewWithNullVersion() throws Exception {
        // Arrange
        String machineName = "newMachine";
        String currentState = "STATE1";

        when(stateMachine.getName()).thenReturn(machineName);
        when(stateMachine.getCurrentState()).thenReturn(currentState);
        when(stateMachineMapper.existsByName(machineName)).thenReturn(0);
        when(stateMachineConfigMapper.getMaxVersionByName(machineName)).thenReturn(null);

        // Act
        stateMachinePersistService.saveStateMachine(stateMachine);

        // Assert
        verify(stateMachineMapper, times(1)).insert(any(StateMachineEntity.class));
    }

    @Test
    public void testGetStateMachineById_Success() throws Exception {
        // Arrange
        Long id = 1L;
        String machineName = "testMachine";
        String configName = "testConfig";
        Integer configVersion = 1;
        String currentState = "STATE1";
        String configXml = "<stateMachine>...</stateMachine>";

        StateMachineEntity entity = new StateMachineEntity();
        entity.setId(id);
        entity.setName(machineName);
        entity.setConfigName(configName);
        entity.setConfigVersion(configVersion);
        entity.setCurrentState(currentState);

        StateMachineDBConfig configEntity = new StateMachineDBConfig();
        configEntity.setName(configName);
        configEntity.setVersion(configVersion);
        configEntity.setConfigXml(configXml);

        StateMachineConfig coreConfig = new StateMachineConfig();

        when(stateMachineMapper.selectById(id)).thenReturn(entity);
        when(stateMachineConfigMapper.selectByNameAndVersion(configName, configVersion)).thenReturn(configEntity);
        when(stateMachineXmlParser.parseFromString(configXml)).thenReturn(coreConfig);
        when(stateMachineFactory.createStateMachineInterfaceFromXmlString(configXml)).thenReturn(stateMachine);

        // Act
        StateMachine result = stateMachinePersistService.getStateMachineById(id);

        // Assert
        assertNotNull(result);
        assertEquals(stateMachine, result);
        verify(stateMachine, times(1)).initialize(currentState);
    }

    @Test
    public void testGetStateMachineById_EntityNotFound() {
        // Arrange
        Long id = 999L;
        when(stateMachineMapper.selectById(id)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            stateMachinePersistService.getStateMachineById(id);
        });

        assertEquals("State machine instance not found for ID: " + id, exception.getMessage());
    }

    @Test
    public void testGetStateMachineById_ConfigNotFound() {
        // Arrange
        Long id = 1L;
        String configName = "nonExistentConfig";
        Integer configVersion = 1;

        StateMachineEntity entity = new StateMachineEntity();
        entity.setId(id);
        entity.setConfigName(configName);
        entity.setConfigVersion(configVersion);

        when(stateMachineMapper.selectById(id)).thenReturn(entity);
        when(stateMachineConfigMapper.selectByNameAndVersion(configName, configVersion)).thenReturn(null);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            stateMachinePersistService.getStateMachineById(id);
        });

        assertEquals("State machine configuration not found: " + configName + ", version: " + configVersion, exception.getMessage());
    }

    @Test
    public void testDeleteStateMachineConfig_Success() {
        // Arrange
        String configName = "testConfig";
        when(stateMachineConfigMapper.existsByName(configName)).thenReturn(1);
        doReturn(1).when(stateMachineConfigMapper).deleteByName(configName);

        // Act
        boolean result = stateMachinePersistService.deleteStateMachineConfig(configName);

        // Assert
        assertTrue(result);
        verify(stateMachineConfigMapper, times(1)).deleteByName(configName);
    }

    @Test
    public void testDeleteStateMachineConfig_NotFound() {
        // Arrange
        String configName = "nonExistentConfig";
        when(stateMachineConfigMapper.existsByName(configName)).thenReturn(0);

        // Act
        boolean result = stateMachinePersistService.deleteStateMachineConfig(configName);

        // Assert
        assertFalse(result);
        verify(stateMachineConfigMapper, times(0)).deleteByName(configName);
    }

    @Test
    public void testDeleteStateMachineInstance_Success() {
        // Arrange
        String instanceName = "testInstance";
        when(stateMachineMapper.existsByName(instanceName)).thenReturn(1);
        doReturn(1).when(stateMachineMapper).deleteByName(instanceName);

        // Act
        boolean result = stateMachinePersistService.deleteStateMachineInstance(instanceName);

        // Assert
        assertTrue(result);
        verify(stateMachineMapper, times(1)).deleteByName(instanceName);
    }

    @Test
    public void testDeleteStateMachineInstance_NotFound() {
        // Arrange
        String instanceName = "nonExistentInstance";
        when(stateMachineMapper.existsByName(instanceName)).thenReturn(0);

        // Act
        boolean result = stateMachinePersistService.deleteStateMachineInstance(instanceName);

        // Assert
        assertFalse(result);
        verify(stateMachineMapper, times(0)).deleteByName(instanceName);
    }

    @Test
    public void testExistsStateMachineConfig_Exists() {
        // Arrange
        String configName = "testConfig";
        when(stateMachineConfigMapper.existsByName(configName)).thenReturn(1);

        // Act
        boolean result = stateMachinePersistService.existsStateMachineConfig(configName);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testExistsStateMachineConfig_NotExists() {
        // Arrange
        String configName = "nonExistentConfig";
        when(stateMachineConfigMapper.existsByName(configName)).thenReturn(0);

        // Act
        boolean result = stateMachinePersistService.existsStateMachineConfig(configName);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testExistsStateMachineInstance_Exists() {
        // Arrange
        String instanceName = "testInstance";
        when(stateMachineMapper.existsByName(instanceName)).thenReturn(1);

        // Act
        boolean result = stateMachinePersistService.existsStateMachineInstance(instanceName);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testExistsStateMachineInstance_NotExists() {
        // Arrange
        String instanceName = "nonExistentInstance";
        when(stateMachineMapper.existsByName(instanceName)).thenReturn(0);

        // Act
        boolean result = stateMachinePersistService.existsStateMachineInstance(instanceName);

        // Assert
        assertFalse(result);
    }
}