package com.jameswushanghai.statemachine.api;

import com.jameswushanghai.statemachine.core.StateMachine;

/**
 * Demo State Machine API Interface
 * Provides start and retry methods to control state machine execution flow
 */
public interface DemoStateMachine extends StateMachine {
    /**
     * Start the state machine
     * @return Current state machine instance, supports method chaining
     */
    DemoStateMachine start();
    
    /**
     * Retry the state machine
     * @return Current state machine instance, supports method chaining
     */
    DemoStateMachine retry();
}