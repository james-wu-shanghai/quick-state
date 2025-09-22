package com.jameswushanghai.statemachine.example;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.core.StateMachine;
import com.jameswushanghai.statemachine.core.StateMachineFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.fail;

/**
 * OrderStateMachineExample的测试类
 */
public class OrderStateMachineExampleTest {

    @Mock
    private StateMachineFactory mockStateMachineFactory;

    @Mock
    private StateMachine mockStateMachine;

    @InjectMocks
    private OrderStateMachineExample orderStateMachineExample;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRunExample() {
        try {
            // 模拟状态机工厂创建状态机
            when(mockStateMachineFactory.createStateMachineInterfaceFromXmlString(anyString())).thenReturn(mockStateMachine);
            
            // 模拟状态机执行动作的返回值
            when(mockStateMachine.execute(eq("PAY"), any(Context.class))).thenReturn("PAID");
            when(mockStateMachine.execute(eq("SHIP"), any(Context.class))).thenReturn("SHIPPED");
            when(mockStateMachine.execute(eq("DELIVER"), any(Context.class))).thenReturn("DELIVERED");
            when(mockStateMachine.execute(eq("COMPLETE"), any(Context.class))).thenReturn("COMPLETED");
            
            // 模拟状态机的当前状态
            when(mockStateMachine.getCurrentState()).thenReturn("CREATED", "PAID", "SHIPPED", "DELIVERED", "COMPLETED");
            
            // 模拟是否为终态
            when(mockStateMachine.isFinalState()).thenReturn(true);

            // 运行示例
            orderStateMachineExample.runExample();

            // 验证方法调用
            verify(mockStateMachineFactory).createStateMachineInterfaceFromXmlString(anyString());
            verify(mockStateMachine).initialize("CREATED");
            verify(mockStateMachine).execute(eq("PAY"), any(Context.class));
            verify(mockStateMachine).execute(eq("SHIP"), any(Context.class));
            verify(mockStateMachine).execute(eq("DELIVER"), any(Context.class));
            verify(mockStateMachine).execute(eq("COMPLETE"), any(Context.class));
            verify(mockStateMachine).isFinalState();
        } catch (Exception e) {
            // 如果抛出异常，则测试失败
            fail("Test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testRunExampleWithException() {
        try {
            // 模拟状态机工厂创建状态机时抛出异常
            when(mockStateMachineFactory.createStateMachineInterfaceFromXmlString(anyString())).thenThrow(new RuntimeException("Creation failed"));

            // 运行示例 - 应该不会抛出异常，而是捕获并记录日志
            orderStateMachineExample.runExample();

            // 验证方法调用
            verify(mockStateMachineFactory).createStateMachineInterfaceFromXmlString(anyString());
            // 后续方法不应该被调用
            verify(mockStateMachine, never()).initialize(anyString());
            verify(mockStateMachine, never()).execute(anyString(), any(Context.class));
        } catch (Exception e) {
            // 如果抛出异常，则测试失败
            fail("Test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testRunExampleWithExecuteException() {
        try {
            // 模拟创建状态机成功，但执行动作时抛出异常
            when(mockStateMachineFactory.createStateMachineInterfaceFromXmlString(anyString())).thenReturn(mockStateMachine);
            
            // 模拟initialize方法正常执行
            doNothing().when(mockStateMachine).initialize("CREATED");
            when(mockStateMachine.getCurrentState()).thenReturn("CREATED");
            
            // 模拟execute方法抛出异常
            when(mockStateMachine.execute(eq("PAY"), any(Context.class))).thenThrow(new RuntimeException("Execution failed"));

            // 运行示例 - 应该不会抛出异常，而是捕获并记录日志
            orderStateMachineExample.runExample();

            // 验证方法调用
            verify(mockStateMachineFactory).createStateMachineInterfaceFromXmlString(anyString());
            verify(mockStateMachine).initialize("CREATED");
            verify(mockStateMachine).execute(eq("PAY"), any(Context.class));
            // 后续的execute不应该被调用
            verify(mockStateMachine, never()).execute(eq("SHIP"), any(Context.class));
            verify(mockStateMachine, never()).execute(eq("DELIVER"), any(Context.class));
            verify(mockStateMachine, never()).execute(eq("COMPLETE"), any(Context.class));
        } catch (Exception e) {
            // 如果抛出异常，则测试失败
            fail("Test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testRunExampleWithDifferentStateTransitions() {
        try {
            // 模拟不同的状态转换路径
            when(mockStateMachineFactory.createStateMachineInterfaceFromXmlString(anyString())).thenReturn(mockStateMachine);
            
            // 模拟状态机执行动作的返回值（支付失败）
            when(mockStateMachine.execute(eq("PAY"), any(Context.class))).thenReturn("FAILED");
            
            // 模拟状态机的当前状态
            when(mockStateMachine.getCurrentState()).thenReturn("CREATED", "FAILED");
            
            // 模拟是否为终态
            when(mockStateMachine.isFinalState()).thenReturn(true);

            // 运行示例
            orderStateMachineExample.runExample();

            // 验证方法调用
            verify(mockStateMachineFactory).createStateMachineInterfaceFromXmlString(anyString());
            verify(mockStateMachine).initialize("CREATED");
            verify(mockStateMachine).execute(eq("PAY"), any(Context.class));
            // 后续的execute不应该被调用，因为状态变为FAILED
            verify(mockStateMachine, never()).execute(eq("SHIP"), any(Context.class));
            verify(mockStateMachine, never()).execute(eq("DELIVER"), any(Context.class));
            verify(mockStateMachine, never()).execute(eq("COMPLETE"), any(Context.class));
            verify(mockStateMachine).isFinalState();
        } catch (Exception e) {
            // 如果抛出异常，则测试失败
            fail("Test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testRunExampleWithNullStateMachine() {
        try {
            // 模拟状态机工厂返回null
            when(mockStateMachineFactory.createStateMachineInterfaceFromXmlString(anyString())).thenReturn(null);

            // 运行示例 - 应该不会抛出异常，而是捕获并记录日志
            orderStateMachineExample.runExample();

            // 验证方法调用
            verify(mockStateMachineFactory).createStateMachineInterfaceFromXmlString(anyString());
            // 后续方法不应该被调用
            verify(mockStateMachine, never()).initialize(anyString());
            verify(mockStateMachine, never()).execute(anyString(), any(Context.class));
        } catch (Exception e) {
            // 如果抛出异常，则测试失败
            fail("Test should not throw exception: " + e.getMessage());
        }
    }
}