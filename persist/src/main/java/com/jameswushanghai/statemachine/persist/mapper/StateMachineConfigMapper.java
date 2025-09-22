package com.jameswushanghai.statemachine.persist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jameswushanghai.statemachine.persist.entity.StateMachineDBConfig;

/**
 * MyBatis Mapper interface for state machine configuration entity database operations
 */
@Mapper
public interface StateMachineConfigMapper {
    
    /**
     * Query state machine configuration by ID
     * @param id Primary key ID
     * @return State machine configuration entity
     */
    StateMachineDBConfig selectById(Long id);
    
    /**
     * Query the latest version of state machine configuration by name
     * @param name Configuration name
     * @return State machine configuration entity
     */
    StateMachineDBConfig selectByName(String name);
    
    /**
     * Query state machine configuration by name and version
     * @param name Configuration name
     * @param version Version number
     * @return State machine configuration entity
     */
    StateMachineDBConfig selectByNameAndVersion(String name, Integer version);
    
    /**
     * Query all state machine configurations
     * @return List of state machine configurations
     */
    List<StateMachineDBConfig> selectAll();
    
    /**
     * Insert state machine configuration
     * @param stateMachineConfig State machine configuration entity
     * @return Number of affected rows
     */
    int insert(StateMachineDBConfig stateMachineConfig);
    
    /**
     * Update state machine configuration
     * @param stateMachineConfig State machine configuration entity
     * @return Number of affected rows
     */
    int update(StateMachineDBConfig stateMachineConfig);
    
    /**
     * Delete state machine configuration by ID
     * @param id Primary key ID
     * @return Number of affected rows
     */
    int deleteById(Long id);
    
    /**
     * Delete state machine configuration by name
     * @param name Configuration name
     * @return Number of affected rows
     */
    int deleteByName(String name);
    
    /**
     * Check if state machine configuration exists
     * @param name Configuration name
     * @return Returns 1 if exists, 0 if not exists
     */
    int existsByName(String name);
    
    /**
     * Check if state machine configuration exists by name and version
     * @param name Configuration name
     * @param version Version number
     * @return Returns 1 if exists, 0 if not exists
     */
    int existsByNameAndVersion(String name, Integer version);
    
    /**
     * Get maximum version number for specified name
     * @param name Configuration name
     * @return Maximum version number
     */
    Integer getMaxVersionByName(String name);
}