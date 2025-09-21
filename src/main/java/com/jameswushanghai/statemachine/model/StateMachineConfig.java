package com.jameswushanghai.statemachine.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * 状态机配置模型
 * 表示整个状态机的配置信息
 */
public class StateMachineConfig {

    // API接口类名
    private String apiInterface;

    
    // 状态机名称
    private String name;
    
    // 存储所有状态配置的Map，键为状态名称
    private Map<String, StateConfig> states = new HashMap<>();
    
    /**
     * 添加状态配置
     * @param stateConfig 状态配置
     */
    public void addState(StateConfig stateConfig) {
        states.put(stateConfig.getName(), stateConfig);
    }
    
    /**
     * 根据状态名称获取状态配置
     * @param stateName 状态名称
     * @return 状态配置，如果找不到返回null
     */
    public StateConfig getState(String stateName) {
        return states.get(stateName);
    }
    
    /**
     * 检查是否包含指定名称的状态
     * @param stateName 状态名称
     * @return 是否包含
     */
    public boolean containsState(String stateName) {
        return states.containsKey(stateName);
    }
    
    /**
     * 获取状态数量
     * @return 状态数量
     */
    public int getStateCount() {
        return states.size();
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for states
    public Map<String, StateConfig> getStates() {
        return Collections.unmodifiableMap(states);
    }

    public void setStates(Map<String, StateConfig> states) {
        this.states = states != null ? states : new HashMap<>();
    }

    // Getter and Setter for apiInterface
    public String getApiInterface() {
        return apiInterface;
    }

    public void setApiInterface(String apiInterface) {
        this.apiInterface = apiInterface;
    }
}