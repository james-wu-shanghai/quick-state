package com.jameswushanghai.statemachine.persist.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * State Machine Configuration Entity
 * Used to store state machine XML configuration and version information in database
 */
public class StateMachineDBConfig {
    
    /**
     * Primary Key ID
     */
    private Long id;
    
    /**
     * State Machine Name
     * Serves as business key, uniquely identifies a state machine configuration
     */
    private String name;
    
    /**
     * State Machine Configuration (XML format)
     * Stores complete configuration information of the state machine
     */
    private String configXml;
    
    /**
     * Version Number
     * Used for configuration version management
     */
    private Integer version;
    
    /**
     * Description
     */
    private String description;
    
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
    
    public String getConfigXml() {
        return configXml;
    }
    
    public void setConfigXml(String configXml) {
        this.configXml = configXml;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
        StateMachineDBConfig that = (StateMachineDBConfig) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(configXml, that.configXml) && Objects.equals(version, that.version) && Objects.equals(description, that.description) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, configXml, version, description, createTime, updateTime);
    }
    
    @Override
    public String toString() {
        return "StateMachineConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", configXml='" + (configXml != null ? configXml.substring(0, Math.min(configXml.length(), 50)) + (configXml.length() > 50 ? "..." : "") : null) + '\'' +
                ", version=" + version +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}