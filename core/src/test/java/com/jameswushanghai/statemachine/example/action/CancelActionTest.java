package com.jameswushanghai.statemachine.example.action;

import com.jameswushanghai.statemachine.core.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * CancelAction的测试类
 */
public class CancelActionTest {

    private CancelAction cancelAction;
    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cancelAction = new CancelAction();
        context = new Context();
    }

    @Test
    public void testDoActionSuccess() {
        // 设置上下文数据
        String orderId = "123456";
        String userId = "user001";
        context.set("orderId", orderId);
        context.set("userId", userId);

        // 执行动作
        String result = cancelAction.doAction(context);

        // 验证结果
        assertEquals("SUCCESS", result);
        assertEquals("CANCELLED", context.get("orderStatus"));
        assertNotNull(context.get("cancellationTime"));
    }

    @Test
    public void testDoActionMissingOrderId() {
        // 缺少订单ID
        String userId = "user001";
        context.set("userId", userId);
        
        // 执行动作
        String result = cancelAction.doAction(context);
        
        // 验证结果
        assertEquals("FAILED", result);
        assertEquals("FAILED", context.get("orderStatus"));
        assertNotNull(context.get("errorMessage"));
    }

    @Test
    public void testDoActionMissingUserId() {
        // 缺少用户ID
        String orderId = "123456";
        context.set("orderId", orderId);
        
        // 执行动作
        String result = cancelAction.doAction(context);
        
        // 验证结果
        assertEquals("FAILED", result);
        assertEquals("FAILED", context.get("orderStatus"));
        assertNotNull(context.get("errorMessage"));
    }

    @Test
    public void testDoActionNullContext() {
        // 测试null上下文
        String result = cancelAction.doAction(null);
        
        // 验证结果
        assertEquals("FAILED", result);
    }

    @Test
    public void testDoActionNullValues() {
        // 测试上下文中的null值
        context.set("orderId", null);
        context.set("userId", null);
        
        // 执行动作
        String result = cancelAction.doAction(context);
        
        // 验证结果
        assertEquals("FAILED", result);
        assertEquals("FAILED", context.get("orderStatus"));
        assertNotNull(context.get("errorMessage"));
    }

    @Test
    public void testDoActionWithException() {
        // 设置一个会导致异常的上下文
        context.set("orderId", 123456); // 整数类型，不是字符串
        context.set("userId", "user001");
        
        // 执行动作
        String result = cancelAction.doAction(context);
        
        // 验证结果
        assertEquals("FAILED", result);
        assertEquals("FAILED", context.get("orderStatus"));
        assertNotNull(context.get("errorMessage"));
    }

    @Test
    public void testDoActionWithDifferentOrderIds() {
        // 测试不同的订单ID
        String[] orderIds = {"order001", "order-123", "订单-中文"};
        String userId = "user001";
        
        for (String orderId : orderIds) {
            context.clear();
            context.set("orderId", orderId);
            context.set("userId", userId);
            
            String result = cancelAction.doAction(context);
            
            assertEquals("SUCCESS", result);
            assertEquals("CANCELLED", context.get("orderStatus"));
            assertNotNull(context.get("cancellationTime"));
        }
    }
}