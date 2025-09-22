package com.jameswushanghai.statemachine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;

/**
 * Start action implementation class
 * Implements Action interface to handle start action and return response code
 */
@Component("startAction")
public class StartAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(StartAction.class);
    
    @Override
    public String doAction(Context context) {
        log.info("Executing start action");
        // Add logic for start action
        context.set("action", "start");
        context.set("timestamp", System.currentTimeMillis());
        
        // Check if it's the first execution, return FAILED if yes, SUCCESS otherwise
        boolean firstExecution = context.getOrDefault("firstExecution", false) instanceof Boolean && 
                                 (Boolean) context.getOrDefault("firstExecution", false);
        
        if (firstExecution) {
            log.info("First execution of start action, returning FAILED");
            // Reset flag to ensure next call succeeds
            context.set("firstExecution", false);
            return "FAILED";
        } else {
            log.info("Not the first execution of start action, returning SUCCESS");
            return "SUCCESS";
        }
    }
}