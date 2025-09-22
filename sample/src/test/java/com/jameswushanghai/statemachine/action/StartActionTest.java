package com.jameswushanghai.statemachine.action;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.action.StartAction;

/**
 * StartAction的测试类
 * 测试开始动作在不同场景下的行为
 */
public class StartActionTest {
    private StartAction startAction;
    private AutoCloseable closeable;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        startAction = new StartAction();
    }

    @Test
    public void testDoAction_firstExecution() {
        // 准备测试数据
        Context context = new Context();
        context.set("firstExecution", true);

        // 执行测试
        String result = startAction.doAction(context);

        // 验证结果
        assertEquals(result, "FAILED", "第一次执行应该返回FAILED");
        assertEquals(context.get("action"), "start", "action字段应该被设置为start");
        assertNotNull(context.get("timestamp"), "timestamp字段应该被设置");
        assertFalse((Boolean) context.get("firstExecution"), "firstExecution标志应该被重置为false");
    }

    @Test
    public void testDoAction_nonFirstExecution() {
        // 准备测试数据
        Context context = new Context();
        context.set("firstExecution", false);

        // 执行测试
        String result = startAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "非第一次执行应该返回SUCCESS");
        assertEquals(context.get("action"), "start", "action字段应该被设置为start");
        assertNotNull(context.get("timestamp"), "timestamp字段应该被设置");
    }

    @Test
    public void testDoAction_noFirstExecutionFlag() {
        // 准备测试数据 - 没有设置firstExecution字段
        Context context = new Context();

        // 执行测试
        String result = startAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "没有设置firstExecution标志时应该默认返回SUCCESS");
        assertEquals(context.get("action"), "start", "action字段应该被设置为start");
        assertNotNull(context.get("timestamp"), "timestamp字段应该被设置");
    }

    @Test
    public void testDoAction_invalidFirstExecutionType() {
        // 准备测试数据 - 设置非法类型的firstExecution
        Context context = new Context();
        context.set("firstExecution", "invalid_type");

        // 执行测试
        String result = startAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "非法类型的firstExecution应该默认返回SUCCESS");
        assertEquals(context.get("action"), "start", "action字段应该被设置为start");
        assertNotNull(context.get("timestamp"), "timestamp字段应该被设置");
    }
}