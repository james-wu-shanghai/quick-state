package com.jameswushanghai.statemachine.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * State machine configuration model
 * Represents configuration information for the entire state machine
 */
public class StateMachineConfig {

    // API interface class name
    private String apiInterface;

    
    // State machine name
    private String name;
    
    // Map to store all state configurations, key is state name
    private Map<String, StateConfig> states = new HashMap<>();
    
    /**
     * Add state configuration
     * @param stateConfig State configuration
     */
    public void addState(StateConfig stateConfig) {
        states.put(stateConfig.getName(), stateConfig);
    }
    
    /**
     * Get state configuration by state name
     * @param stateName State name
     * @return State configuration, returns null if not found
     */
    public StateConfig getState(String stateName) {
        return states.get(stateName);
    }
    
    /**
     * Check if contains a state with the specified name
     * @param stateName State name
     * @return Whether it contains
     */
    public boolean containsState(String stateName) {
        return states.containsKey(stateName);
    }
    
    /**
     * Get the number of states
     * @return Number of states
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