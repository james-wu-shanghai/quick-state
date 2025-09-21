package com.jameswushanghai.statemachine.example.action;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支付动作实现
 */
@Component("payAction")
public class PayAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(PayAction.class);
    
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
                context.set("paymentStatus", "FAILED");
                context.set("errorMessage", "缺少必要参数: orderId或userId");
                return "FAILED";
            }
            
            log.info("执行支付动作，订单ID: {}, 用户ID: {}", orderId, userId);
            
            // 模拟支付处理逻辑
            // 实际应用中这里会调用支付服务进行真实的支付操作
            
            // 设置支付相关信息到上下文中
            context.set("paymentTime", System.currentTimeMillis());
            context.set("paymentStatus", "SUCCESS");
            return "SUCCESS";
        } catch (Exception e) {
            log.error("支付动作执行失败: {}", e.getMessage(), e);
            if (context != null) {
                context.set("paymentStatus", "FAILED");
                context.set("errorMessage", e.getMessage());
            }
            return "FAILED";
        }
    }
}