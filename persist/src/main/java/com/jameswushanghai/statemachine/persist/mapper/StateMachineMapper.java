package com.jameswushanghai.statemachine.persist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jameswushanghai.statemachine.persist.entity.StateMachineEntity;

/**
 * MyBatis Mapper interface for state machine instance entity database operations
 */
@Mapper
public interface StateMachineMapper {
    
    /**
     * Query state machine instance by ID
     * @param id Primary key ID
     * @return State machine instance entity
     */
    StateMachineEntity selectById(Long id);
    
    /**
     * Query state machine instance by name
     * @param name Instance name
     * @return State machine instance entity
     */
    StateMachineEntity selectByName(String name);
    
    /**
     * Query all associated state machine instances by configuration name
     * @param configName Configuration name
     * @return List of state machine instances
     */
    List<StateMachineEntity> selectByConfigName(String configName);
    
    /**
     * Query all state machine instances
     * @return List of state machine instances
     */
    List<StateMachineEntity> selectAll();
    
    /**
     * Insert state machine instance
     * @param stateMachineEntity State machine instance entity
     * @return Number of affected rows
     */
    int insert(StateMachineEntity stateMachineEntity);
    
    /**
     * Update state machine instance
     * @param stateMachineEntity State machine instance entity
     * @return Number of affected rows
     */
    int update(StateMachineEntity stateMachineEntity);
    
    /**
     * Delete state machine instance by ID
     * @param id Primary key ID
     * @return Number of affected rows
     */
    int deleteById(Long id);
    
    /**
     * Delete state machine instance by name
     * @param name Instance name
     * @return Number of affected rows
     */
    int deleteByName(String name);
    
    /**
     * Delete all associated state machine instances by configuration name
     * @param configName Configuration name
     * @return Number of affected rows
     */
    int deleteByConfigName(String configName);
    
    /**
     * Check if state machine instance exists
     * @param name Instance name
     * @return Returns 1 if exists, 0 if not exists
     */
    int existsByName(String name);
}