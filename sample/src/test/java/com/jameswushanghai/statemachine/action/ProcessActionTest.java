package com.jameswushanghai.statemachine.action;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.action.ProcessAction;

/**
 * Test class for ProcessAction
 * Tests the behavior of the process action
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
        // Prepare test data
        Context context = new Context();

        // Execute test
        String result = processAction.doAction(context);

        // Verify results
        assertEquals(result, "DONE", "Process action should return DONE");
        assertEquals(context.get("action"), "process", "action field should be set to process");
        assertNotNull(context.get("processTime"), "processTime field should be set");

    }

    @Test
    public void testDoAction_withExistingContextData() {
        // Prepare test data - containing existing data
        Context context = new Context();
        context.set("existingData", "test_value");
        Long startTime = System.currentTimeMillis();
        context.set("timestamp", startTime);

        // 执行测试
        String result = processAction.doAction(context);

        // Verify results
        assertEquals(result, "DONE", "Process action should return DONE");
        assertEquals(context.get("action"), "process", "action field should be set to process");
        assertEquals(context.get("existingData"), "test_value", "Existing data should remain unchanged");
        assertNotNull(context.get("processTime"), "processTime field should be set");
        assertNotNull(context.get("timestamp"), "timestamp field should remain unchanged");

    }

    @Test
    public void testDoAction_threadSleepBehavior() {
        // Prepare test data
        Context context = new Context();
        long beforeTime = System.currentTimeMillis();


        // Execute test
        String result = processAction.doAction(context);
        long afterTime = System.currentTimeMillis();

        // Verify results
        assertEquals(result, "DONE", "Process action should return DONE");
        assertTrue(afterTime - beforeTime >= 90, "Process action should execute for at least approximately 100 milliseconds");
        assertNotNull(context.get("processTime"), "processTime field should be set");
    }
}