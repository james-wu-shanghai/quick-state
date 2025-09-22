package com.jameswushanghai.statemachine.persist.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jameswushanghai.statemachine.core.StateMachine;
import com.jameswushanghai.statemachine.core.StateMachineFactory;
import com.jameswushanghai.statemachine.parser.StateMachineXmlParser;
import com.jameswushanghai.statemachine.persist.entity.StateMachineDBConfig;
import com.jameswushanghai.statemachine.persist.entity.StateMachineEntity;
import com.jameswushanghai.statemachine.persist.mapper.StateMachineConfigMapper;
import com.jameswushanghai.statemachine.persist.mapper.StateMachineMapper;

/**
 * State machine persistence service that provides creation, saving and loading of state machine instances
 */
@Service
public class StateMachinePersistService {

    private static final Logger log = LoggerFactory.getLogger(StateMachinePersistService.class);

    @Autowired
    private StateMachineMapper stateMachineMapper;

    @Autowired
    private StateMachineConfigMapper stateMachineConfigMapper;

    @Autowired
    private StateMachineXmlParser stateMachineXmlParser;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    /**
     * Create a new StateMachine by machine name
     * 
     * @param machineName State machine name
     * @return Created StateMachine instance
     * @throws Exception Creation exception
     */
    public StateMachine createStateMachine(String machineName) throws Exception {
        log.info("Creating new StateMachine by machine name: {}", machineName);

        // Get state machine configuration from database
        StateMachineDBConfig configEntity = stateMachineConfigMapper.selectByName(machineName);

        if (configEntity == null) {
            throw new IllegalArgumentException("State machine configuration not found for name: " + machineName);
        }

        // Create StateMachine from XML string
        return stateMachineFactory.createStateMachineInterfaceFromXmlString(configEntity.getConfigXml());
    }

    /**
     * Save StateMachine to database as StateMachineEntity and return id
     * 
     * @param stateMachine StateMachine instance to save
     * @return Saved entity ID
     * @throws Exception Save exception
     */
    @Transactional
    public Long saveStateMachine(StateMachine stateMachine) throws Exception {
        log.info("Saving StateMachine to database, name: {}", stateMachine.getName());

        // Check if instance already exists
        if (stateMachineMapper.existsByName(stateMachine.getName()) > 0) {
            // Update existing instance
            StateMachineEntity existingEntity = stateMachineMapper.selectByName(stateMachine.getName());
            existingEntity.setCurrentState(stateMachine.getCurrentState());
            existingEntity.setUpdateTime(LocalDateTime.now());
            stateMachineMapper.update(existingEntity);
            log.info("State machine instance updated successfully, instance ID: {}", existingEntity.getId());
            return existingEntity.getId();
        } else {
            // Create new instance
            StateMachineEntity newEntity = new StateMachineEntity();
            newEntity.setName(stateMachine.getName());
            newEntity.setConfigName(stateMachine.getName()); // Assume config name same as instance name

            // Get latest config version
            Integer latestVersion = stateMachineConfigMapper.getMaxVersionByName(stateMachine.getName());
            newEntity.setConfigVersion(latestVersion != null ? latestVersion : 1);

            newEntity.setCurrentState(stateMachine.getCurrentState());
            newEntity.setCreateTime(LocalDateTime.now());
            newEntity.setUpdateTime(LocalDateTime.now());

            stateMachineMapper.insert(newEntity);
            log.info("State machine instance saved successfully, instance ID: {}", newEntity.getId());
            return newEntity.getId();
        }
    }

    /**
     * Read StateMachineEntity by id and assemble into StateMachine
     * 
     * @param id State machine entity ID
     * @return Assembled StateMachine instance
     * @throws Exception Read exception
     */
    public StateMachine getStateMachineById(Long id) throws Exception {
        log.info("Reading StateMachine by ID: {}", id);

        // Read StateMachineEntity from database
        StateMachineEntity entity = stateMachineMapper.selectById(id);

        if (entity == null) {
            throw new IllegalArgumentException("State machine instance not found for ID: " + id);
        }

        // Read associated configuration
        StateMachineDBConfig configEntity = stateMachineConfigMapper.selectByNameAndVersion(entity.getConfigName(),
                entity.getConfigVersion());

        if (configEntity == null) {
            throw new IllegalStateException(
                    "State machine configuration not found: " + entity.getConfigName() + ", version: " + entity.getConfigVersion());
        }

        // Parse configuration XML
        com.jameswushanghai.statemachine.model.StateMachineConfig coreConfig = stateMachineXmlParser
                .parseFromString(configEntity.getConfigXml());

        // Create StateMachine from XML string
        StateMachine stateMachine = stateMachineFactory
                .createStateMachineInterfaceFromXmlString(configEntity.getConfigXml());

        // Initialize current state of state machine
        stateMachine.initialize(entity.getCurrentState());

        log.info("Successfully read StateMachine by ID, name: {}, current state: {}", entity.getName(), entity.getCurrentState());
        return stateMachine;
    }

    /**
     * Delete state machine configuration
     * 
     * @param configName Configuration name
     * @return Whether deletion was successful
     */
    @Transactional
    public boolean deleteStateMachineConfig(String configName) {
        log.info("Deleting state machine configuration, config name: {}", configName);
        // First check if configuration exists
        if (stateMachineConfigMapper.existsByName(configName) == 0) {
            return false;
        }

        // Delete configuration
        stateMachineConfigMapper.deleteByName(configName);
        return true;
    }

    /**
     * Delete state machine instance
     * 
     * @param instanceName Instance name
     * @return Whether deletion was successful
     */
    @Transactional
    public boolean deleteStateMachineInstance(String instanceName) {
        log.info("Deleting state machine instance, instance name: {}", instanceName);
        // First check if instance exists
        if (stateMachineMapper.existsByName(instanceName) == 0) {
            return false;
        }

        // Delete instance
        stateMachineMapper.deleteByName(instanceName);
        return true;
    }

    /**
     * Check if state machine configuration exists
     * 
     * @param configName Configuration name
     * @return Whether exists
     */
    public boolean existsStateMachineConfig(String configName) {
        return stateMachineConfigMapper.existsByName(configName) > 0;
    }

    /**
     * Check if state machine instance exists
     * 
     * @param instanceName Instance name
     * @return Whether exists
     */
    public boolean existsStateMachineInstance(String instanceName) {
        return stateMachineMapper.existsByName(instanceName) > 0;
    }
}