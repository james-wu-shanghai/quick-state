package com.jameswushanghai.statemachine.example.action;

import com.jameswushanghai.statemachine.core.Action;
import com.jameswushanghai.statemachine.core.Context;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发货动作实现
 */
@Component("shipAction")
public class ShipAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(ShipAction.class);
    
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
                context.set("shippingStatus", "FAILED");
                context.set("errorMessage", "缺少必要参数: orderId或userId");
                return "FAILED";
            }
            
            log.info("执行发货动作，订单ID: {}, 用户ID: {}", orderId, userId);
            
            // 模拟发货处理逻辑
            // 实际应用中这里会调用物流服务进行真实的发货操作
            
            // 设置发货相关信息到上下文中
            context.set("shippingTime", System.currentTimeMillis());
            context.set("shippingStatus", "SUCCESS");
            context.set("logisticsCompany", "顺丰快递");
            context.set("trackingNumber", "SF1234567890");
            return "SUCCESS";
        } catch (Exception e) {
            log.error("发货动作执行失败: {}", e.getMessage(), e);
            if (context != null) {
                context.set("shippingStatus", "FAILED");
                context.set("errorMessage", e.getMessage());
            }
            return "FAILED";
        }
    }
}