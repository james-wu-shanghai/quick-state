package com.jameswushanghai.statemachine.action;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.action.ProcessAction;

/**
 * ProcessAction的测试类
 * 测试处理动作的行为
 */
public class ProcessActionTest {
    private ProcessAction processAction;
    private AutoCloseable closeable;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        processAction = new ProcessAction();
    }

    @Test
    public void testDoAction_basicBehavior() {
        // 准备测试数据
        Context context = new Context();

        // 执行测试
        String result = processAction.doAction(context);

        // 验证结果
        assertEquals(result, "DONE", "处理动作应该返回DONE");
        assertEquals(context.get("action"), "process", "action字段应该被设置为process");
        assertNotNull(context.get("processTime"), "processTime字段应该被设置");
    }

    @Test
    public void testDoAction_withExistingContextData() {
        // 准备测试数据 - 包含现有数据
        Context context = new Context();
        context.set("existingData", "test_value");
        Long startTime = System.currentTimeMillis();
        context.set("timestamp", startTime);

        // 执行测试
        String result = processAction.doAction(context);

        // 验证结果
        assertEquals(result, "DONE", "处理动作应该返回DONE");
        assertEquals(context.get("action"), "process", "action字段应该被设置为process");
        assertEquals(context.get("existingData"), "test_value", "现有数据应该保持不变");
        assertNotNull(context.get("processTime"), "processTime字段应该被设置");
        assertNotNull(context.get("timestamp"), "timestamp字段应该保持不变");
    }

    @Test
    public void testDoAction_threadSleepBehavior() {
        // 准备测试数据
        Context context = new Context();
        long beforeTime = System.currentTimeMillis();

        // 执行测试
        String result = processAction.doAction(context);
        long afterTime = System.currentTimeMillis();

        // 验证结果
        assertEquals(result, "DONE", "处理动作应该返回DONE");
        assertTrue(afterTime - beforeTime >= 90, "处理动作应该至少执行约100毫秒");
        assertNotNull(context.get("processTime"), "processTime字段应该被设置");
    }
}