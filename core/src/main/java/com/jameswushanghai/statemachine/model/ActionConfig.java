package com.jameswushanghai.statemachine.model;

import java.util.List;

/**
 * Action configuration model
 * Represents action configuration defined in a state
 */
public class ActionConfig {
    
    // Action name
    private String name;
    
    // Referenced Spring bean name
    private String ref;
    
    // List of possible next states
    private List<NextState> nextStates;
    
    // Whether to automatically move to the next state
    private boolean autoMoveForward = false;

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for ref
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    // Getter and Setter for nextStates
    public List<NextState> getNextStates() {
        return nextStates;
    }

    public void setNextStates(List<NextState> nextStates) {
        this.nextStates = nextStates;
    }
    
    // Getter and Setter for autoMoveForward
    public boolean isAutoMoveForward() {
        return autoMoveForward;
    }
    
    public void setAutoMoveForward(boolean autoMoveForward) {
        this.autoMoveForward = autoMoveForward;
    }
}