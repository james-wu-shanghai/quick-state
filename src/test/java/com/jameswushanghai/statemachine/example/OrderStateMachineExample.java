package com.jameswushanghai.statemachine.example;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.core.StateMachine;
import com.jameswushanghai.statemachine.core.StateMachineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单状态机示例
 * 展示如何使用状态机框架
 */
@Component
public class OrderStateMachineExample {

    private static final Logger log = LoggerFactory.getLogger(OrderStateMachineExample.class);
    
    @Autowired
    private StateMachineFactory stateMachineFactory;
    
    /**
     * 运行订单状态机示例
     */
    public void runExample() {
        try {
            // 这里使用XML字符串创建状态机
            String xmlConfig = "<stateMachine name='orderStateMachine'>" +
                    "  <state name='CREATED'>" +
                    "    <action name='PAY' ref='payAction'>" +
                    "      <nextState respCode='SUCCESS' state='PAID'/>" +
                    "      <nextState respCode='FAILED' state='FAILED'/>" +
                    "    </action>" +
                    "    <action name='CANCEL' ref='cancelAction'>" +
                    "      <nextState respCode='SUCCESS' state='CANCELED'/>" +
                    "    </action>" +
                    "  </state>" +
                    "  <state name='PAID'>" +
                    "    <action name='SHIP' ref='shipAction'>" +
                    "      <nextState respCode='SUCCESS' state='SHIPPED'/>" +
                    "      <nextState respCode='FAILED' state='FAILED'/>" +
                    "    </action>" +
                    "  </state>" +
                    "  <state name='SHIPPED'>" +
                    "    <action name='DELIVER' ref='deliverAction'>" +
                    "      <nextState respCode='SUCCESS' state='DELIVERED'/>" +
                    "      <nextState respCode='FAILED' state='FAILED'/>" +
                    "    </action>" +
                    "  </state>" +
                    "  <state name='DELIVERED'>" +
                    "    <action name='COMPLETE' ref='completeAction'>" +
                    "      <nextState respCode='SUCCESS' state='COMPLETED'/>" +
                    "    </action>" +
                    "  </state>" +
                    "  <state name='CANCELED'/>" +
                    "  <state name='FAILED'/>" +
                    "  <state name='COMPLETED'/>" +
                    "</stateMachine>";
            
            // 创建状态机
            StateMachine stateMachine = stateMachineFactory.createStateMachineFromXmlString(xmlConfig);
            
            // 初始化状态机
            stateMachine.initialize("CREATED");
            log.info("订单状态机初始化完成，当前状态: {}", stateMachine.getCurrentState());
            
            // 创建上下文
            Context context = new Context();
            context.set("orderId", "123456");
            context.set("userId", "user001");
            
            // 执行支付动作
            String newState = stateMachine.execute("PAY", context);
            log.info("执行支付后，订单状态变为: {}", newState);
            
            // 只有在PAID状态下才能执行SHIP动作
            if ("PAID".equals(stateMachine.getCurrentState())) {
                // 执行发货动作
                newState = stateMachine.execute("SHIP", context);
                log.info("执行发货后，订单状态变为: {}", newState);
                
                // 只有在SHIPPED状态下才能执行DELIVER动作
                if ("SHIPPED".equals(stateMachine.getCurrentState())) {
                    // 执行收货动作
                    newState = stateMachine.execute("DELIVER", context);
                    log.info("执行收货后，订单状态变为: {}", newState);
                    
                    // 只有在DELIVERED状态下才能执行COMPLETE动作
                    if ("DELIVERED".equals(stateMachine.getCurrentState())) {
                        // 执行完成动作
                        newState = stateMachine.execute("COMPLETE", context);
                        log.info("执行完成后，订单状态变为: {}", newState);
                    }
                }
            }
            
            // 检查是否为终态
            log.info("订单状态机是否处于终态: {}", stateMachine.isFinalState());
            
        } catch (Exception e) {
            log.error("订单状态机示例运行失败: {}", e.getMessage(), e);
        }
    }
}