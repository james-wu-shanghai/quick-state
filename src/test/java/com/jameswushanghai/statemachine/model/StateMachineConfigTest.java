package com.jameswushanghai.statemachine.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * StateMachineConfig的测试类
 */
public class StateMachineConfigTest {

    private StateMachineConfig config;

    @Before
    public void setUp() {
        config = new StateMachineConfig();
    }

    @Test
    public void testSetAndGetName() {
        // 测试设置和获取名称
        String name = "testMachine";
        config.setName(name);
        assertEquals(name, config.getName());

        // 测试设置null名称
        config.setName(null);
        assertNull(config.getName());
    }

    @Test
    public void testAddAndGetState() {
        // 测试添加和获取状态
        String stateName = "INITIAL";
        StateConfig stateConfig = new StateConfig();
        stateConfig.setName(stateName);

        config.addState(stateConfig);
        assertEquals(stateConfig, config.getState(stateName));
        assertNotNull(config.getStates());
        assertEquals(1, config.getStates().size());
    }

    @Test
    public void testContainsState() {
        // 测试containsState方法
        String stateName = "INITIAL";
        StateConfig stateConfig = new StateConfig();
        stateConfig.setName(stateName);
        config.addState(stateConfig);

        assertTrue(config.containsState(stateName));
        assertFalse(config.containsState("NON_EXISTENT"));

        // 测试空名称的情况
        assertFalse(config.containsState(null));
    }

    @Test
    public void testAddMultipleStates() {
        // 测试添加多个状态
        StateConfig state1 = new StateConfig();
        state1.setName("S1");
        
        StateConfig state2 = new StateConfig();
        state2.setName("S2");
        
        StateConfig state3 = new StateConfig();
        state3.setName("S3");

        config.addState(state1);
        config.addState(state2);
        config.addState(state3);

        assertEquals(3, config.getStates().size());
        assertEquals(state1, config.getState("S1"));
        assertEquals(state2, config.getState("S2"));
        assertEquals(state3, config.getState("S3"));
    }

    @Test
    public void testAddDuplicateState() {
        // 测试添加重名的状态（应该覆盖）
        StateConfig state1 = new StateConfig();
        state1.setName("S1");
        
        StateConfig state2 = new StateConfig();
        state2.setName("S1"); // 相同的名称

        config.addState(state1);
        config.addState(state2);

        assertEquals(1, config.getStates().size());
        assertEquals(state2, config.getState("S1")); // 应该是最后添加的那个
    }

    @Test
    public void testGetStateWithNullName() {
        // 测试获取名称为null的状态
        StateConfig state = new StateConfig();
        state.setName(null);
        config.addState(state);
        
        assertEquals(state, config.getState(null));
    }

    @Test
    public void testGetStatesImmutability() {
        // 测试getStates返回的Map的不可变性
        StateConfig state1 = new StateConfig();
        state1.setName("S1");
        config.addState(state1);
        
        try {
            config.getStates().put("S2", new StateConfig());
            fail("应该抛出UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // 预期的异常
        }
    }

    @Test
    public void testStateMachineConfigEquality() {
        // 测试状态机配置的相等性（这个类没有重写equals方法，所以测试引用相等）
        StateMachineConfig config1 = new StateMachineConfig();
        StateMachineConfig config2 = new StateMachineConfig();
        StateMachineConfig config3 = config1;
        
        assertFalse(config1.equals(config2));
        assertTrue(config1.equals(config3));
    }

    @Test
    public void testAddStateWithNull() {
        // 测试添加null状态
        try {
            config.addState(null);
            // 如果代码允许添加null，那么测试继续
            assertNull(config.getState(null));
        } catch (NullPointerException e) {
            // 如果代码不允许添加null，捕获异常
        }
    }

    @Test
    public void testSetStatesWithNull() {
        // 测试设置null状态Map
        config.setStates(null);
        assertNotNull(config.getStates());
        assertEquals(0, config.getStates().size());
    }

    @Test
    public void testSetStatesWithValidMap() {
        // 测试设置有效的状态Map
        Map<String, StateConfig> statesMap = new HashMap<>();
        StateConfig state = new StateConfig();
        state.setName("TEST_STATE");
        statesMap.put("TEST_STATE", state);
        
        config.setStates(statesMap);
        assertTrue(config.containsState("TEST_STATE"));
        assertEquals(state, config.getState("TEST_STATE"));
    }
}