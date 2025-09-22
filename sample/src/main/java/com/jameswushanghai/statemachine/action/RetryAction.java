package com.jameswushanghai.statemachine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;

/**
 * Retry action implementation class
 * Implements Action interface to handle retry action and return response code
 */
@Component("retryAction")
public class RetryAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(RetryAction.class);
    
    @Override
    public String doAction(Context context) {
        log.info("Executing retry action");
        // Add logic for retry action
        context.set("action", "retry");
        Object retryCountObj = context.getOrDefault("retryCount", 0);
        int retryCount = (retryCountObj instanceof Integer) ? (Integer) retryCountObj : 0;
        context.set("retryCount", retryCount + 1);
        context.set("timestamp", System.currentTimeMillis());
        
        // Return success response code for state transition
        return "SUCCESS";
    }
}