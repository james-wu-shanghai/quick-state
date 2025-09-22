package com.jameswushanghai.statemachine.action;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.action.CompleteAction;

/**
 * Test class for CompleteAction
 * Tests the behavior and time calculation logic of the complete action
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
        // Prepare test data
        Context context = new Context();

        // Execute test
        String result = completeAction.doAction(context);

        // Verify results
        assertEquals(result, "SUCCESS", "Complete action should return SUCCESS");
        assertEquals(context.get("action"), "complete", "action field should be set to complete");
        assertNotNull(context.get("completeTime"), "completeTime field should be set");
    }

    @Test
    public void testDoAction_withTimestamps() {
        // Prepare test data - containing startTime and processTime
        Context context = new Context();
        long startTime = System.currentTimeMillis() - 1000; // 1 second ago
        long processTime = startTime + 500; // 500 milliseconds later
        context.set("timestamp", startTime);
        context.set("processTime", processTime);

        // 执行测试
        String result = completeAction.doAction(context);
        long completeTime = (Long) context.get("completeTime");

        // Verify results
        assertEquals(result, "SUCCESS", "Complete action should return SUCCESS");
        assertEquals(context.get("action"), "complete", "action field should be set to complete");
        assertNotNull(context.get("completeTime"), "completeTime field should be set");
        
        // Verify time difference calculation is correct (allowing for some time error)
        assertTrue(completeTime >= processTime, "completeTime should be greater than or equal to processTime");
    }

    @Test
    public void testDoAction_withExistingContextData() {
        // Prepare test data - containing existing data
        Context context = new Context();
        context.set("existingData", "test_value");


        // Execute test
        String result = completeAction.doAction(context);

        // Verify results
        assertEquals(result, "SUCCESS", "Complete action should return SUCCESS");
        assertEquals(context.get("action"), "complete", "action field should be set to complete");
        assertEquals(context.get("existingData"), "test_value", "Existing data should remain unchanged");
        assertNotNull(context.get("completeTime"), "completeTime field should be set");
    }

    @Test
    public void testDoAction_missingTimestamp() {
        // Prepare test data - missing timestamp
        Context context = new Context();
        context.set("processTime", System.currentTimeMillis() - 500);


        // Execute test
        String result = completeAction.doAction(context);

        // Verify results
        assertEquals(result, "SUCCESS", "Complete action should return SUCCESS");
        assertEquals(context.get("action"), "complete", "action field should be set to complete");
        assertNotNull(context.get("completeTime"), "completeTime field should be set");
    }

    @Test
    public void testDoAction_missingProcessTime() {
        // Prepare test data - missing processTime
        Context context = new Context();
        context.set("timestamp", System.currentTimeMillis() - 1000);


        // Execute test
        String result = completeAction.doAction(context);

        // Verify results
        assertEquals(result, "SUCCESS", "Complete action should return SUCCESS");
        assertEquals(context.get("action"), "complete", "action field should be set to complete");
        assertNotNull(context.get("completeTime"), "completeTime field should be set");
    }

    @Test
    public void testDoAction_invalidTimeTypes() {
        // Prepare test data - setting invalid types for timestamps
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