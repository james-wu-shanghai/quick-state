package com.jameswushanghai.statemachine;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Simple TestNG test class without any framework dependencies
 */
public class VerySimpleTest {
    
    public VerySimpleTest() {
        // Default constructor
        System.out.println("VerySimpleTest instantiated successfully");
    }
    
    @Test
    public void testVerySimple() {
        System.out.println("Test method executed");
        assertTrue(true, "Simple test passed");
    }
}