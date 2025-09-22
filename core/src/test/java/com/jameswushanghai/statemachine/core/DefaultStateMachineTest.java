package com.jameswushanghai.statemachine.core;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import com.jameswushanghai.statemachine.model.ActionConfig;
import com.jameswushanghai.statemachine.model.NextState;
import com.jameswushanghai.statemachine.model.StateConfig;
import com.jameswushanghai.statemachine.model.StateMachineConfig;

/**
 * DefaultStateMachine的测试类
 */
public class DefaultStateMachineTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private Action mockAction;

    @Mock
    private StateMachineConfig mockStateMachineConfig;

    @Mock
    private StateConfig mockStateConfig;

    @Mock
    private ActionConfig mockActionConfig;

    @InjectMocks
    private DefaultStateMachine stateMachine;

    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stateMachine = new DefaultStateMachine(mockStateMachineConfig, applicationContext);
        context = new Context();
        
        // 配置mockStateMachineConfig.containsState总是返回true，因为这是测试环境
        when(mockStateMachineConfig.containsState(anyString())).thenReturn(true);
        
        // 配置applicationContext.containsBean总是返回true，因为这是测试环境
        when(applicationContext.containsBean(anyString())).thenReturn(true);
        
        // 配置applicationContext.getBean总是返回mockAction，因为这是测试环境
        when(applicationContext.getBean(anyString(), eq(Action.class))).thenReturn(mockAction);
    }

    @Test
    public void testInitialize() {
        // 测试初始化状态机
        String initialState = "INITIAL";
        stateMachine.initialize(initialState);
        assertEquals(initialState, stateMachine.getCurrentState());
    }

    @Test
    public void testInitializeWithInvalidState() {
        // 测试无效的初始状态
        String invalidState = "INVALID_STATE";
        when(mockStateMachineConfig.containsState(invalidState)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> {
            stateMachine.initialize(invalidState);
        });
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        // 模拟成功执行动作的场景
        String currentState = "CREATED";
        String actionName = "PAY";
        String respCode = "SUCCESS";
        String nextState = "PAID";

        stateMachine.initialize(currentState);

        // 设置mock行为
        when(mockStateMachineConfig.getState(currentState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(actionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn("payAction");
        when(applicationContext.getBean("payAction", Action.class)).thenReturn(mockAction);
        when(mockAction.doAction(context)).thenReturn(respCode);

        // 模拟下一个状态
        NextState nextStateObj = new NextState();
        nextStateObj.setRespCode(respCode);
        nextStateObj.setState(nextState);
        List<NextState> nextStates = new ArrayList<>();
        nextStates.add(nextStateObj);
        when(mockActionConfig.getNextStates()).thenReturn(nextStates);

        // 执行动作
        String resultState = stateMachine.execute(actionName, context);

        // 验证结果
        assertEquals(nextState, resultState);
        assertEquals(nextState, stateMachine.getCurrentState());
        verify(mockAction).doAction(context);
    }

    @Test
    public void testExecuteFailure() throws Exception {
        // 模拟执行动作失败的场景
        String currentState = "CREATED";
        String actionName = "PAY";
        String respCode = "FAILED";
        String nextState = "FAILED";

        stateMachine.initialize(currentState);

        // 设置mock行为
        when(mockStateMachineConfig.getState(currentState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(actionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn("payAction");
        when(applicationContext.getBean("payAction", Action.class)).thenReturn(mockAction);
        when(mockAction.doAction(context)).thenReturn(respCode);

        // 模拟下一个状态
        NextState nextStateObj = new NextState();
        nextStateObj.setRespCode(respCode);
        nextStateObj.setState(nextState);
        List<NextState> nextStates = new ArrayList<>();
        nextStates.add(nextStateObj);
        when(mockActionConfig.getNextStates()).thenReturn(nextStates);

        // 执行动作
        String resultState = stateMachine.execute(actionName, context);

        // 验证结果
        assertEquals(nextState, resultState);
        assertEquals(nextState, stateMachine.getCurrentState());
        verify(mockAction).doAction(context);
    }

    @Test
    public void testExecuteNoNextStateFound() throws Exception {
        // 模拟没有找到下一个状态的场景
        String currentState = "CREATED";
        String actionName = "PAY";
        String respCode = "UNKNOWN";

        stateMachine.initialize(currentState);

        // 设置mock行为
        when(mockStateMachineConfig.getState(currentState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(actionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn("payAction");
        when(applicationContext.getBean("payAction", Action.class)).thenReturn(mockAction);
        when(mockAction.doAction(context)).thenReturn(respCode);
        when(mockActionConfig.getNextStates()).thenReturn(new ArrayList<>());

        // 执行动作
        String resultState = stateMachine.execute(actionName, context);

        // 验证结果 - 状态不变
        assertEquals(currentState, resultState);
        assertEquals(currentState, stateMachine.getCurrentState());
        verify(mockAction).doAction(context);
    }

    @Test
    public void testExecuteStateNotInitialized() {
        // 测试状态机未初始化时执行动作
        String actionName = "PAY";
        assertThrows(IllegalStateException.class, () -> {
            stateMachine.execute(actionName, context);
        });
    }

    @Test
    public void testExecuteActionNotFound() throws Exception {
        // 测试当前状态下未找到指定动作
        String currentState = "CREATED";
        String actionName = "UNKNOWN_ACTION";

        stateMachine.initialize(currentState);
        when(mockStateMachineConfig.getState(currentState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(actionName)).thenReturn(null);

        assertThrows(Exception.class, () -> {
            stateMachine.execute(actionName, context);
        });
    }

    @Test
    public void testExecuteWithInvalidCurrentState() {
        // 测试无效的当前状态
        String currentState = "INVALID_STATE";
        String actionName = "PAY";
        
        stateMachine.setCurrentState(currentState);
        when(mockStateMachineConfig.getState(currentState)).thenReturn(null);
        
        assertThrows(IllegalStateException.class, () -> {
            stateMachine.execute(actionName, context);
        });
    }

    @Test
    public void testExecuteWithNullApplicationContext() {
        // 测试applicationContext为null的情况
        String currentState = "CREATED";
        String actionName = "PAY";
        
        stateMachine.setApplicationContext(null);
        stateMachine.initialize(currentState);
        when(mockStateMachineConfig.getState(currentState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(actionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn("payAction");
        
        assertThrows(IllegalStateException.class, () -> {
            stateMachine.execute(actionName, context);
        });
    }

    @Test
    public void testExecuteWithNonExistentBean() {
        // 测试动作的Spring bean不存在的情况
        String currentState = "CREATED";
        String actionName = "PAY";
        String beanName = "nonExistentBean";
        
        stateMachine.initialize(currentState);
        when(mockStateMachineConfig.getState(currentState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(actionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn(beanName);
        when(applicationContext.containsBean(beanName)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> {
            stateMachine.execute(actionName, context);
        });
    }

    @Test
    public void testIsFinalState() throws Exception {
        // 测试是否为终态
        String finalState = "COMPLETED";
        stateMachine.initialize(finalState);

        // 模拟状态配置中没有动作，视为终态
        when(mockStateMachineConfig.getState(finalState)).thenReturn(mockStateConfig);
        when(mockStateConfig.hasActions()).thenReturn(false);

        assertTrue(stateMachine.isFinalState());

        // 模拟状态配置中有动作，不是终态
        String nonFinalState = "CREATED";
        stateMachine.initialize(nonFinalState);
        when(mockStateMachineConfig.getState(nonFinalState)).thenReturn(mockStateConfig);
        when(mockStateConfig.hasActions()).thenReturn(true);

        assertFalse(stateMachine.isFinalState());
    }

    @Test
    public void testIsFinalStateWithNullCurrentState() {
        // 测试当前状态为null的情况
        stateMachine.setCurrentState(null);
        assertFalse(stateMachine.isFinalState());
    }

    @Test
    public void testIsFinalStateWithNullStateConfig() {
        // 测试状态配置为null的情况
        String state = "UNKNOWN_STATE";
        stateMachine.setCurrentState(state);
        when(mockStateMachineConfig.getState(state)).thenReturn(null);
        assertTrue(stateMachine.isFinalState());
    }

    @Test
    public void testGettersAndSetters() {
        // 测试getter和setter方法
        stateMachine.setName("newName");
        assertEquals("newName", stateMachine.getName());

        stateMachine.setCurrentState("newState");
        assertEquals("newState", stateMachine.getCurrentState());

        stateMachine.setConfig(mockStateMachineConfig);
        assertEquals(mockStateMachineConfig, stateMachine.getConfig());

        stateMachine.setApplicationContext(applicationContext);
        assertEquals(applicationContext, stateMachine.getApplicationContext());
    }

    @Test
    public void testExecuteWithDifferentResponseCodes() throws Exception {
        // 测试不同响应码导致的状态转换
        String currentState = "CREATED";
        String actionName = "PAY";

        stateMachine.initialize(currentState);

        // 设置mock行为
        when(mockStateMachineConfig.getState(currentState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(actionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn("payAction");
        when(applicationContext.getBean("payAction", Action.class)).thenReturn(mockAction);

        // 准备不同响应码和对应的下一个状态
        // SUCCESS响应
        when(mockAction.doAction(context)).thenReturn("SUCCESS");
        NextState successNextState = new NextState();
        successNextState.setRespCode("SUCCESS");
        successNextState.setState("PAID");
        
        // FAILED响应
        NextState failedNextState = new NextState();
        failedNextState.setRespCode("FAILED");
        failedNextState.setState("PAY_FAILED");
        
        List<NextState> nextStates = new ArrayList<>();
        nextStates.add(successNextState);
        nextStates.add(failedNextState);
        
        when(mockActionConfig.getNextStates()).thenReturn(nextStates);

        // 测试SUCCESS响应
        when(mockAction.doAction(context)).thenReturn("SUCCESS");
        String resultState = stateMachine.execute(actionName, context);
        assertEquals("PAID", resultState);
        assertEquals("PAID", stateMachine.getCurrentState());

        // 重新初始化并测试FAILED响应
        stateMachine.initialize(currentState);
        when(mockAction.doAction(context)).thenReturn("FAILED");
        resultState = stateMachine.execute(actionName, context);
        assertEquals("PAY_FAILED", resultState);
        assertEquals("PAY_FAILED", stateMachine.getCurrentState());

        // 重新初始化并测试未知响应码
        stateMachine.initialize(currentState);
        when(mockAction.doAction(context)).thenReturn("UNKNOWN");
        resultState = stateMachine.execute(actionName, context);
        assertEquals(currentState, resultState); // 状态不变
        assertEquals(currentState, stateMachine.getCurrentState());
    }

    @Test
    public void testExecuteWithNullNextStates() throws Exception {
        // 测试nextStates为null的情况
        String currentState = "CREATED";
        String actionName = "PAY";
        String respCode = "SUCCESS";
        
        stateMachine.initialize(currentState);
        when(mockStateMachineConfig.getState(currentState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(actionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn("payAction");
        when(applicationContext.getBean("payAction", Action.class)).thenReturn(mockAction);
        when(mockAction.doAction(context)).thenReturn(respCode);
        when(mockActionConfig.getNextStates()).thenReturn(null);
        
        String resultState = stateMachine.execute(actionName, context);
        assertEquals(currentState, resultState); // 状态不变
        assertEquals(currentState, stateMachine.getCurrentState());
    }

    @Test
    public void testExecuteWithAutoMoveForward() throws Exception {
        // 测试autoMoveForward功能
        String initialState = "INITIAL";
        String firstActionName = "START";
        String firstRespCode = "SUCCESS";
        String middleState = "PROCESSING";
        String secondActionName = "PROCESS";
        String secondRespCode = "DONE";
        String finalState = "COMPLETED";

        stateMachine.initialize(initialState);

        // 配置第一个状态和动作
        when(mockStateMachineConfig.getState(initialState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(firstActionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn("startAction");
        when(applicationContext.getBean("startAction", Action.class)).thenReturn(mockAction);
        when(mockAction.doAction(context)).thenReturn(firstRespCode);
        
        // 配置autoMoveForward为true
        when(mockActionConfig.isAutoMoveForward()).thenReturn(true);

        // 配置第一个动作的下一个状态
        NextState firstNextState = new NextState();
        firstNextState.setRespCode(firstRespCode);
        firstNextState.setState(middleState);
        List<NextState> firstNextStates = new ArrayList<>();
        firstNextStates.add(firstNextState);
        when(mockActionConfig.getNextStates()).thenReturn(firstNextStates);

        // 创建并配置第二个状态和动作
        StateConfig middleStateConfig = new StateConfig();
        middleStateConfig.setName(middleState);
        ActionConfig secondActionConfig = new ActionConfig();
        secondActionConfig.setName(secondActionName);
        secondActionConfig.setRef("processAction");
        middleStateConfig.setActions(new ArrayList<>());
        middleStateConfig.getActions().add(secondActionConfig);
        
        // 配置第二个动作的mock
        Action secondMockAction = Mockito.mock(Action.class);
        when(applicationContext.getBean("processAction", Action.class)).thenReturn(secondMockAction);
        when(secondMockAction.doAction(context)).thenReturn(secondRespCode);

        // 配置第二个动作的下一个状态
        NextState secondNextState = new NextState();
        secondNextState.setRespCode(secondRespCode);
        secondNextState.setState(finalState);
        List<NextState> secondNextStates = new ArrayList<>();
        secondNextStates.add(secondNextState);
        secondActionConfig.setNextStates(secondNextStates);

        // 配置mockStateMachineConfig返回中间状态配置
        when(mockStateMachineConfig.getState(middleState)).thenReturn(middleStateConfig);

        // 执行第一个动作，应该自动执行第二个动作
        String resultState = stateMachine.execute(firstActionName, context);

        // 验证结果 - 最终状态应该是finalState
        assertEquals(finalState, resultState);
        assertEquals(finalState, stateMachine.getCurrentState());
        
        // 验证两个动作都被执行了
        verify(mockAction).doAction(context);
        verify(secondMockAction).doAction(context);
    }

    @Test
    public void testExecuteWithAutoMoveForwardButMultipleActions() throws Exception {
        // 测试autoMoveForward为true但下一个状态有多个动作的情况
        String initialState = "INITIAL";
        String firstActionName = "START";
        String firstRespCode = "SUCCESS";
        String middleState = "PROCESSING";

        stateMachine.initialize(initialState);

        // 配置第一个状态和动作
        when(mockStateMachineConfig.getState(initialState)).thenReturn(mockStateConfig);
        when(mockStateConfig.findAction(firstActionName)).thenReturn(mockActionConfig);
        when(mockActionConfig.getRef()).thenReturn("startAction");
        when(applicationContext.getBean("startAction", Action.class)).thenReturn(mockAction);
        when(mockAction.doAction(context)).thenReturn(firstRespCode);
        
        // 配置autoMoveForward为true
        when(mockActionConfig.isAutoMoveForward()).thenReturn(true);

        // 配置第一个动作的下一个状态
        NextState firstNextState = new NextState();
        firstNextState.setRespCode(firstRespCode);
        firstNextState.setState(middleState);
        List<NextState> firstNextStates = new ArrayList<>();
        firstNextStates.add(firstNextState);
        when(mockActionConfig.getNextStates()).thenReturn(firstNextStates);

        // 创建并配置中间状态，包含多个动作
        StateConfig middleStateConfig = new StateConfig();
        middleStateConfig.setName(middleState);
        ActionConfig secondActionConfig1 = new ActionConfig();
        secondActionConfig1.setName("PROCESS1");
        ActionConfig secondActionConfig2 = new ActionConfig();
        secondActionConfig2.setName("PROCESS2");
        middleStateConfig.setActions(new ArrayList<>());
        middleStateConfig.getActions().add(secondActionConfig1);
        middleStateConfig.getActions().add(secondActionConfig2);

        // 配置mockStateMachineConfig返回中间状态配置
        when(mockStateMachineConfig.getState(middleState)).thenReturn(middleStateConfig);

        // 执行第一个动作，由于下一个状态有多个动作，不应该自动执行
        String resultState = stateMachine.execute(firstActionName, context);

        // 验证结果 - 最终状态应该是middleState
        assertEquals(middleState, resultState);
        assertEquals(middleState, stateMachine.getCurrentState());
        
        // 验证只有第一个动作被执行
        verify(mockAction).doAction(context);
    }

}