package com.jameswushanghai.statemachine.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * NextState的测试类
 */
public class NextStateTest {

    private NextState nextState;

    @Before
    public void setUp() {
        nextState = new NextState();
    }

    @Test
    public void testSetAndGetRespCode() {
        // 测试设置和获取响应码
        String respCode = "SUCCESS";
        nextState.setRespCode(respCode);
        assertEquals(respCode, nextState.getRespCode());

        // 测试设置null响应码
        nextState.setRespCode(null);
        assertNull(nextState.getRespCode());
    }

    @Test
    public void testSetAndGetState() {
        // 测试设置和获取状态
        String state = "PAID";
        nextState.setState(state);
        assertEquals(state, nextState.getState());

        // 测试设置null状态
        nextState.setState(null);
        assertNull(nextState.getState());
    }

    @Test
    public void testNextStateEquality() {
        // 测试下一个状态的相等性（这个类没有重写equals方法，所以测试引用相等）
        NextState state1 = new NextState();
        NextState state2 = new NextState();
        NextState state3 = state1;
        
        assertFalse(state1.equals(state2));
        assertTrue(state1.equals(state3));
    }

    @Test
    public void testMultipleNextStates() {
        // 测试多个NextState实例的独立性
        NextState state1 = new NextState();
        NextState state2 = new NextState();
        
        state1.setRespCode("SUCCESS");
        state1.setState("SUCCESS_STATE");
        
        state2.setRespCode("FAILED");
        state2.setState("FAILED_STATE");
        
        assertEquals("SUCCESS", state1.getRespCode());
        assertEquals("SUCCESS_STATE", state1.getState());
        assertEquals("FAILED", state2.getRespCode());
        assertEquals("FAILED_STATE", state2.getState());
    }

    @Test
    public void testSetEmptyValues() {
        // 测试设置空字符串值
        nextState.setRespCode("");
        nextState.setState("");
        
        assertEquals("", nextState.getRespCode());
        assertEquals("", nextState.getState());
    }

    @Test
    public void testSetSpecialCharacters() {
        // 测试设置包含特殊字符的值
        String specialRespCode = "SUCCESS-123!@#";
        String specialState = "STATE.WITH.DOTS";
        
        nextState.setRespCode(specialRespCode);
        nextState.setState(specialState);
        
        assertEquals(specialRespCode, nextState.getRespCode());
        assertEquals(specialState, nextState.getState());
    }
}