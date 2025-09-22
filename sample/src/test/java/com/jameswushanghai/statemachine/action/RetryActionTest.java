package com.jameswushanghai.statemachine.action;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.action.RetryAction;

/**
 * RetryAction的测试类
 * 测试重试动作的行为和重试计数
 */
public class RetryActionTest {
    private RetryAction retryAction;
    private AutoCloseable closeable;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        retryAction = new RetryAction();
    }

    @Test
    public void testDoAction_firstRetry() {
        // 准备测试数据 - 首次重试，没有设置retryCount
        Context context = new Context();

        // 执行测试
        String result = retryAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "重试动作应该返回SUCCESS");
        assertEquals(context.get("action"), "retry", "action字段应该被设置为retry");
        assertEquals(context.get("retryCount"), 1, "首次重试后retryCount应该为1");
        assertNotNull(context.get("timestamp"), "timestamp字段应该被设置");
    }

    @Test
    public void testDoAction_subsequentRetries() {
        // 准备测试数据 - 已存在retryCount
        Context context = new Context();
        context.set("retryCount", 5);

        // 执行测试
        String result = retryAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "重试动作应该返回SUCCESS");
        assertEquals(context.get("action"), "retry", "action字段应该被设置为retry");
        assertEquals(context.get("retryCount"), 6, "重试后retryCount应该递增1");
        assertNotNull(context.get("timestamp"), "timestamp字段应该被设置");
    }

    @Test
    public void testDoAction_withExistingContextData() {
        // 准备测试数据 - 包含现有数据
        Context context = new Context();
        context.set("existingData", "test_value");

        // 执行测试
        String result = retryAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "重试动作应该返回SUCCESS");
        assertEquals(context.get("action"), "retry", "action字段应该被设置为retry");
        assertEquals(context.get("retryCount"), 1, "retryCount应该为1");
        assertEquals(context.get("existingData"), "test_value", "现有数据应该保持不变");
        assertNotNull(context.get("timestamp"), "timestamp字段应该被设置");
    }

    @Test
    public void testDoAction_invalidRetryCountType() {
        // 准备测试数据 - 设置非法类型的retryCount
        Context context = new Context();
        context.set("retryCount", "invalid_type");

        // 执行测试
        String result = retryAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "重试动作应该返回SUCCESS");
        assertEquals(context.get("action"), "retry", "action字段应该被设置为retry");
        assertEquals(context.get("retryCount"), 1, "非法类型的retryCount应该被视为0并递增到1");
        assertNotNull(context.get("timestamp"), "timestamp字段应该被设置");
    }
}