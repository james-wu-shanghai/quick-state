package com.jameswushanghai.statemachine.example.action;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 取消动作实现
 */
@Component("cancelAction")
public class CancelAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(CancelAction.class);
    
    @Override
    public String doAction(Context context) {
        try {
            // 检查上下文是否为null
            if (context == null) {
                return "FAILED";
            }
            
            // 获取订单ID和用户ID
            String orderId = context.get("orderId", String.class);
            String userId = context.get("userId", String.class);
            
            // 检查必要参数是否存在
            if (orderId == null || userId == null) {
                context.set("orderStatus", "FAILED");
                context.set("errorMessage", "缺少必要参数: orderId或userId");
                return "FAILED";
            }
            
            log.info("执行取消订单动作，订单ID: {}, 用户ID: {}", orderId, userId);
            
            // 模拟取消订单逻辑
            // 实际应用中这里会调用订单服务进行真实的取消操作
            
            // 设置取消相关信息到上下文中
            context.set("cancellationTime", System.currentTimeMillis());
            context.set("orderStatus", "CANCELLED");
            context.set("cancelReason", "用户主动取消");
            
            return "SUCCESS";
        } catch (Exception e) {
            log.error("取消订单动作执行失败: {}", e.getMessage(), e);
            if (context != null) {
                context.set("orderStatus", "FAILED");
                context.set("errorMessage", e.getMessage());
            }
            return "FAILED";
        }
    }
}