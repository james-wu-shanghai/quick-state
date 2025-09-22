package com.jameswushanghai.statemachine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;

/**
 * 完成动作实现类
 * 实现Action接口，处理complete动作并返回响应码
 */
@Component("completeAction")
public class CompleteAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(CompleteAction.class);
    
    @Override
    public String doAction(Context context) {
        log.info("执行完成动作");
        // 添加完成动作的逻辑
        context.set("action", "complete");
        context.set("completeTime", System.currentTimeMillis());
        
        // 获取处理过程中的信息
        Object startTimeObj = context.get("timestamp");
        Object processTimeObj = context.get("processTime");
        
        if (startTimeObj != null && processTimeObj != null) {
            try {
                // 尝试将时间戳转换为Long类型
                long startTime = convertToLong(startTimeObj);
                long processTime = convertToLong(processTimeObj);
                long totalTime = (Long) context.get("completeTime") - startTime;
                long processingTime = processTime - startTime;
                
                log.info("整个流程耗时: {}ms", totalTime);
                log.info("处理阶段耗时: {}ms", processingTime);
            } catch (NumberFormatException | ClassCastException e) {
                // 处理类型转换错误
                log.warn("无法计算流程耗时，时间戳格式不正确: {}", e.getMessage());
            }
        }
        
        log.info("任务已成功完成");
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