package com.jameswushanghai.statemachine.action;

import org.mockito.MockitoAnnotations;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jameswushanghai.statemachine.core.Context;

/**
 * Test class for StartAction
 * Tests the behavior of the start action in different scenarios
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
        // Prepare test data
        Context context = new Context();
        context.set("firstExecution", true);

        // Execute test
        String result = startAction.doAction(context);

        // Verify results
        assertEquals(result, "FAILED", "First execution should return FAILED");
        assertEquals(context.get("action"), "start", "action field should be set to start");
        assertNotNull(context.get("timestamp"), "timestamp field should be set");
        assertFalse((Boolean) context.get("firstExecution"), "firstExecution flag should be reset to false");

    }

    @Test
    public void testDoAction_nonFirstExecution() {
        // Prepare test data
        Context context = new Context();
        context.set("firstExecution", false);

        // 执行测试
        String result = startAction.doAction(context);

        // Verify results
        assertEquals(result, "SUCCESS", "Non-first execution should return SUCCESS");
        assertEquals(context.get("action"), "start", "action field should be set to start");
        assertNotNull(context.get("timestamp"), "timestamp field should be set");

    }

    @Test
    public void testDoAction_noFirstExecutionFlag() {
        // Prepare test data - no firstExecution field set
        Context context = new Context();


        // Execute test
        String result = startAction.doAction(context);

        // Verify results
        assertEquals(result, "SUCCESS", "Should return SUCCESS by default when no firstExecution flag is set");
        assertEquals(context.get("action"), "start", "action field should be set to start");
        assertNotNull(context.get("timestamp"), "timestamp field should be set");

    }

    @Test
    public void testDoAction_invalidFirstExecutionType() {
        // Prepare test data - setting invalid type for firstExecution
        Context context = new Context();
        context.set("firstExecution", "invalid_type");


        // Execute test
        String result = startAction.doAction(context);

        // Verify results
        assertEquals(result, "SUCCESS", "Should return SUCCESS by default for invalid type of firstExecution");
        assertEquals(context.get("action"), "start", "action field should be set to start");
        assertNotNull(context.get("timestamp"), "timestamp field should be set");
    }
}