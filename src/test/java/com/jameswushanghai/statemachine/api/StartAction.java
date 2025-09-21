package com.jameswushanghai.statemachine.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;

/**
 * 开始动作实现类
 * 实现Action接口，处理start动作并返回响应码
 */
@Component("startAction")
public class StartAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(StartAction.class);
    
    @Override
    public String doAction(Context context) {
        log.info("执行开始动作");
        // 添加开始动作的逻辑
        context.set("action", "start");
        context.set("timestamp", System.currentTimeMillis());
        
        // 检查是否是第一次执行，如果是则返回失败，否则返回成功
        boolean firstExecution = context.getOrDefault("firstExecution", false) instanceof Boolean && 
                                 (Boolean) context.getOrDefault("firstExecution", false);
        
        if (firstExecution) {
            log.info("第一次执行start动作，返回失败");
            // 重置标志，确保下次调用成功
            context.set("firstExecution", false);
            return "FAILED";
        } else {
            log.info("非第一次执行start动作，返回成功");
            return "SUCCESS";
        }
    }
}

/**
 * 重试动作实现类
 * 实现Action接口，处理retry动作并返回响应码
 */
@Component("retryAction")
class RetryAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(RetryAction.class);
    
    @Override
    public String doAction(Context context) {
        log.info("执行重试动作");
        // 添加重试动作的逻辑
        context.set("action", "retry");
        Object retryCountObj = context.getOrDefault("retryCount", 0);
        int retryCount = (retryCountObj instanceof Integer) ? (Integer) retryCountObj : 0;
        context.set("retryCount", retryCount + 1);
        context.set("timestamp", System.currentTimeMillis());
        
        // 返回成功的响应码，用于状态转换
        return "SUCCESS";
    }
}