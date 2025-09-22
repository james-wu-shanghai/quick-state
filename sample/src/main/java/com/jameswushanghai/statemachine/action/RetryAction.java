package com.jameswushanghai.statemachine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;

/**
 * 重试动作实现类
 * 实现Action接口，处理retry动作并返回响应码
 */
@Component("retryAction")
public class RetryAction implements Action {
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