package com.jameswushanghai.statemachine.action;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.action.CompleteAction;

/**
 * CompleteAction的测试类
 * 测试完成动作的行为和时间计算逻辑
 */
public class CompleteActionTest {
    private CompleteAction completeAction;
    private AutoCloseable closeable;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        completeAction = new CompleteAction();
    }

    @Test
    public void testDoAction_basicBehavior() {
        // 准备测试数据
        Context context = new Context();

        // 执行测试
        String result = completeAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "完成动作应该返回SUCCESS");
        assertEquals(context.get("action"), "complete", "action字段应该被设置为complete");
        assertNotNull(context.get("completeTime"), "completeTime字段应该被设置");
    }

    @Test
    public void testDoAction_withTimestamps() {
        // 准备测试数据 - 包含startTime和processTime
        Context context = new Context();
        long startTime = System.currentTimeMillis() - 1000; // 1秒前
        long processTime = startTime + 500; // 500毫秒后
        context.set("timestamp", startTime);
        context.set("processTime", processTime);

        // 执行测试
        String result = completeAction.doAction(context);
        long completeTime = (Long) context.get("completeTime");

        // 验证结果
        assertEquals(result, "SUCCESS", "完成动作应该返回SUCCESS");
        assertEquals(context.get("action"), "complete", "action字段应该被设置为complete");
        assertNotNull(context.get("completeTime"), "completeTime字段应该被设置");
        
        // 验证时间差计算是否正确（允许一定的时间误差）
        assertTrue(completeTime >= processTime, "completeTime应该大于或等于processTime");
    }

    @Test
    public void testDoAction_withExistingContextData() {
        // 准备测试数据 - 包含现有数据
        Context context = new Context();
        context.set("existingData", "test_value");

        // 执行测试
        String result = completeAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "完成动作应该返回SUCCESS");
        assertEquals(context.get("action"), "complete", "action字段应该被设置为complete");
        assertEquals(context.get("existingData"), "test_value", "现有数据应该保持不变");
        assertNotNull(context.get("completeTime"), "completeTime字段应该被设置");
    }

    @Test
    public void testDoAction_missingTimestamp() {
        // 准备测试数据 - 缺少timestamp
        Context context = new Context();
        context.set("processTime", System.currentTimeMillis() - 500);

        // 执行测试
        String result = completeAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "完成动作应该返回SUCCESS");
        assertEquals(context.get("action"), "complete", "action字段应该被设置为complete");
        assertNotNull(context.get("completeTime"), "completeTime字段应该被设置");
    }

    @Test
    public void testDoAction_missingProcessTime() {
        // 准备测试数据 - 缺少processTime
        Context context = new Context();
        context.set("timestamp", System.currentTimeMillis() - 1000);

        // 执行测试
        String result = completeAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "完成动作应该返回SUCCESS");
        assertEquals(context.get("action"), "complete", "action字段应该被设置为complete");
        assertNotNull(context.get("completeTime"), "completeTime字段应该被设置");
    }

    @Test
    public void testDoAction_invalidTimeTypes() {
        // 准备测试数据 - 设置非法类型的时间戳
        Context context = new Context();
        context.set("timestamp", "invalid_timestamp");
        context.set("processTime", "invalid_process_time");

        // 执行测试
        String result = completeAction.doAction(context);

        // 验证结果
        assertEquals(result, "SUCCESS", "完成动作应该返回SUCCESS");
        assertEquals(context.get("action"), "complete", "action字段应该被设置为complete");
        assertNotNull(context.get("completeTime"), "completeTime字段应该被设置");
    }
}