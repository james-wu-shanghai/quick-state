package com.jameswushanghai.statemachine.example.action;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 完成订单动作实现
 */
@Component("completeAction")
public class CompleteAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(CompleteAction.class);
    
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
            
            log.info("执行完成订单动作，订单ID: {}, 用户ID: {}", orderId, userId);
            
            // 模拟订单完成处理逻辑
            // 实际应用中这里会执行订单完成后的业务逻辑，如归档订单、计算佣金等
            
            // 设置完成相关信息到上下文中
            context.set("completionTime", System.currentTimeMillis());
            context.set("orderStatus", "COMPLETED");
            
            // 输出订单处理的完整信息
            log.info("订单[{}]处理完成，用户ID: {}, 支付时间: {}, 发货时间: {}, 收货时间: {}, 完成时间: {}",
                    orderId,
                    userId,
                    context.get("paymentTime"),
                    context.get("shipTime"),
                    context.get("deliverTime"),
                    context.get("completionTime"));
            
            return "SUCCESS";
        } catch (Exception e) {
            log.error("完成订单动作执行失败: {}", e.getMessage(), e);
            if (context != null) {
                context.set("orderStatus", "FAILED");
                context.set("errorMessage", e.getMessage());
            }
            return "FAILED";
        }
    }
}