package com.jameswushanghai.statemachine.core;

/**
 * State machine action interface
 * All specific action classes need to implement this interface
 */
public interface Action {
    
    /**
     * Execute action
     * @param context Context object containing data during state machine execution
     * @return Response code used to determine the next state
     */
    String doAction(Context context);
}