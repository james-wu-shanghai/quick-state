package com.jameswushanghai.statemachine.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ActionConfig的测试类
 */
public class ActionConfigTest {

    private ActionConfig actionConfig;

    @Before
    public void setUp() {
        actionConfig = new ActionConfig();
    }

    @Test
    public void testSetAndGetName() {
        // 测试设置和获取名称
        String name = "PAY";
        actionConfig.setName(name);
        assertEquals(name, actionConfig.getName());

        // 测试设置null名称
        actionConfig.setName(null);
        assertNull(actionConfig.getName());
    }

    @Test
    public void testSetAndGetRef() {
        // 测试设置和获取引用
        String ref = "payAction";
        actionConfig.setRef(ref);
        assertEquals(ref, actionConfig.getRef());

        // 测试设置null引用
        actionConfig.setRef(null);
        assertNull(actionConfig.getRef());
    }

    @Test
    public void testSetAndGetNextStates() {
        // 测试设置和获取下一个状态列表
        List<NextState> nextStates = new ArrayList<>();
        NextState nextState1 = new NextState();
        nextState1.setRespCode("SUCCESS");
        nextState1.setState("PAID");
        nextStates.add(nextState1);

        actionConfig.setNextStates(nextStates);
        assertEquals(nextStates, actionConfig.getNextStates());
        assertEquals(1, actionConfig.getNextStates().size());

        // 测试设置null下一个状态列表
        actionConfig.setNextStates(null);
        assertNull(actionConfig.getNextStates());
    }

    @Test
    public void testAddMultipleNextStates() {
        // 测试添加多个下一个状态
        List<NextState> nextStates = new ArrayList<>();
        NextState state1 = new NextState();
        state1.setRespCode("SUCCESS");
        state1.setState("SUCCESS_STATE");
        
        NextState state2 = new NextState();
        state2.setRespCode("FAILED");
        state2.setState("FAILED_STATE");
        
        nextStates.add(state1);
        nextStates.add(state2);

        actionConfig.setNextStates(nextStates);

        assertEquals(2, actionConfig.getNextStates().size());
        assertEquals(state1, actionConfig.getNextStates().get(0));
        assertEquals(state2, actionConfig.getNextStates().get(1));
    }

    @Test
    public void testActionConfigEquality() {
        // 测试动作配置的相等性（这个类没有重写equals方法，所以测试引用相等）
        ActionConfig config1 = new ActionConfig();
        ActionConfig config2 = new ActionConfig();
        ActionConfig config3 = config1;
        
        assertFalse(config1.equals(config2));
        assertTrue(config1.equals(config3));
    }

    @Test
    public void testAddNextStatesOneByOne() {
        // 测试逐个添加下一个状态
        List<NextState> nextStates = new ArrayList<>();
        actionConfig.setNextStates(nextStates);
        
        NextState state1 = new NextState();
        state1.setRespCode("SUCCESS");
        state1.setState("SUCCESS_STATE");
        nextStates.add(state1);
        
        NextState state2 = new NextState();
        state2.setRespCode("FAILED");
        state2.setState("FAILED_STATE");
        nextStates.add(state2);
        
        assertEquals(2, actionConfig.getNextStates().size());
    }

    @Test
    public void testNextStatesImmutability() {
        // 测试getNextStates返回的列表是否可变
        List<NextState> nextStates = new ArrayList<>();
        actionConfig.setNextStates(nextStates);
        
        // 应该可以添加新元素，因为返回的是原始列表的引用
        NextState state = new NextState();
        state.setRespCode("TEST");
        state.setState("TEST_STATE");
        actionConfig.getNextStates().add(state);
        
        assertEquals(1, nextStates.size());
        assertEquals(state, nextStates.get(0));
    }

    @Test
    public void testEmptyNextStatesList() {
        // 测试空的下一个状态列表
        List<NextState> nextStates = new ArrayList<>();
        actionConfig.setNextStates(nextStates);
        
        assertNotNull(actionConfig.getNextStates());
        assertTrue(actionConfig.getNextStates().isEmpty());
    }
    
    @Test
    public void testAutoMoveForwardDefaultValue() {
        // 测试autoMoveForward的默认值
        assertFalse(actionConfig.isAutoMoveForward());
    }
    
    @Test
    public void testSetAndGetAutoMoveForwardTrue() {
        // 测试设置和获取autoMoveForward为true
        actionConfig.setAutoMoveForward(true);
        assertTrue(actionConfig.isAutoMoveForward());
    }
    
    @Test
    public void testSetAndGetAutoMoveForwardFalse() {
        // 测试设置和获取autoMoveForward为false
        actionConfig.setAutoMoveForward(false);
        assertFalse(actionConfig.isAutoMoveForward());
        
        // 先设置为true，再设置为false
        actionConfig.setAutoMoveForward(true);
        assertTrue(actionConfig.isAutoMoveForward());
        actionConfig.setAutoMoveForward(false);
        assertFalse(actionConfig.isAutoMoveForward());
    }
}