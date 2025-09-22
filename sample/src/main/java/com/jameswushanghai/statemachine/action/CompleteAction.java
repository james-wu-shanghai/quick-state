package com.jameswushanghai.statemachine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;

/**
 * Complete action implementation class
 * Implements Action interface to handle complete action and return response code
 */
@Component("completeAction")
public class CompleteAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(CompleteAction.class);
    
    @Override
    public String doAction(Context context) {
        log.info("Executing complete action");
        // Add logic for complete action
        context.set("action", "complete");
        context.set("completeTime", System.currentTimeMillis());
        
        // Get information from the processing
        Object startTimeObj = context.get("timestamp");
        Object processTimeObj = context.get("processTime");
        
        if (startTimeObj != null && processTimeObj != null) {
            try {
                // Try to convert timestamps to Long type
                long startTime = convertToLong(startTimeObj);
                long processTime = convertToLong(processTimeObj);
                long totalTime = (Long) context.get("completeTime") - startTime;
                long processingTime = processTime - startTime;
                
                log.info("Total process time: {}ms", totalTime);
                log.info("Processing stage time: {}ms", processingTime);
            } catch (NumberFormatException | ClassCastException e) {
                // Handle type conversion errors
                log.warn("Unable to calculate process time, timestamp format is incorrect: {}", e.getMessage());
            }
        }
        
        log.info("Task has been successfully completed");
        return "SUCCESS";
    }
    
    /**
     * 将对象转换为Long类型
     * @param obj 要转换的对象
     * @return 转换后的Long值
     * @throws NumberFormatException 如果转换失败
     */
    private long convertToLong(Object obj) {
        if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof String) {
            return Long.parseLong((String) obj);
        } else if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        } else {
            throw new ClassCastException("Cannot convert " + obj.getClass().getName() + " to Long");
        }
    }
}