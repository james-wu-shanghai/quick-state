package com.jameswushanghai.statemachine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;

/**
 * Process action implementation class
 * Implements Action interface to handle process action and return response code
 */
@Component("processAction")
public class ProcessAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(ProcessAction.class);
    
    @Override
    public String doAction(Context context) {
        log.info("Executing process action");
        // Add logic for process action
        context.set("action", "process");
        context.set("processTime", System.currentTimeMillis());
        
        // Simulate processing
        log.info("Processing data...");
        try {
            Thread.sleep(100); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("Processing completed, ready to proceed to next stage");
        return "DONE";
    }
}