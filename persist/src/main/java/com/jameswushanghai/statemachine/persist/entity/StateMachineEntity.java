package com.jameswushanghai.statemachine.persist.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * State Machine Instance Entity
 * Used to store state machine instance information and current state in database
 */
public class StateMachineEntity {
    
    /**
     * Primary Key ID
     */
    private Long id;
    
    /**
     * State Machine Instance Name
     * Serves as business key, uniquely identifies a state machine instance
     */
    private String name;
    
    /**
     * Associated Configuration Name
     * References the name field in StateMachineConfig
     */
    private String configName;
    
    /**
     * Associated Configuration Version
     * References the version field in StateMachineConfig
     */
    private Integer configVersion;
    
    /**
     * Current State
     * Stores the current state of the state machine
     */
    private String currentState;
    
    /**
     * Create Time
     */
    private LocalDateTime createTime;
    
    /**
     * Update Time
     */
    private LocalDateTime updateTime;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getConfigName() {
        return configName;
    }
    
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    
    public Integer getConfigVersion() {
        return configVersion;
    }
    
    public void setConfigVersion(Integer configVersion) {
        this.configVersion = configVersion;
    }
    
    public String getCurrentState() {
        return currentState;
    }
    
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateMachineEntity that = (StateMachineEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(configName, that.configName) && Objects.equals(configVersion, that.configVersion) && Objects.equals(currentState, that.currentState) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, configName, configVersion, currentState, createTime, updateTime);
    }
    
    @Override
    public String toString() {
        return "StateMachineEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", configName='" + configName + '\'' +
                ", configVersion=" + configVersion +
                ", currentState='" + currentState + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}