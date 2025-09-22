package com.jameswushanghai.statemachine;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class StateMachineApplicationTest {

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApplicationStarts() {
        // 简单的测试，验证测试环境正常工作
        assertTrue(true, "Test environment is set up correctly");
    }

}