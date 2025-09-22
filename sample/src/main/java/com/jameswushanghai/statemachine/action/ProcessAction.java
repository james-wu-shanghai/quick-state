package com.jameswushanghai.statemachine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;

/**
 * 处理动作实现类
 * 实现Action接口，处理process动作并返回响应码
 */
@Component("processAction")
public class ProcessAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(ProcessAction.class);
    
    @Override
    public String doAction(Context context) {
        log.info("执行处理动作");
        // 添加处理动作的逻辑
        context.set("action", "process");
        context.set("processTime", System.currentTimeMillis());
        
        // 模拟处理过程
        log.info("正在处理数据...");
        try {
            Thread.sleep(100); // 模拟处理时间
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("处理完成，准备进入下一阶段");
        return "DONE";
    }
}