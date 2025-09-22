package com.jameswushanghai.statemachine.core;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

/**
 * StateMachineApiProxy的测试类
 */
public class StateMachineApiProxyTest {

    @Mock
    private StateMachine stateMachine;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private StateMachineApiProxy stateMachineApiProxy;

    private Context context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        context = new Context();
        // 重新创建StateMachineApiProxy，因为InjectMocks可能不能正确注入所有构造函数参数
        stateMachineApiProxy = new StateMachineApiProxy(stateMachine, applicationContext, context);
    }

    /**
     * 测试构造函数正确初始化各个字段
     */
    @Test
    public void testConstructor() {
        StateMachineApiProxy proxy = new StateMachineApiProxy(stateMachine, applicationContext, context);
        assertNotNull(proxy.getStateMachine());
        assertEquals(stateMachine, proxy.getStateMachine());
    }

    /**
     * 测试createProxy方法当apiInterface为null时直接返回stateMachine
     */
    @Test
    public void testCreateProxyWithNullApiInterface() throws Exception {
        Object result = StateMachineApiProxy.createProxy(stateMachine, applicationContext, null);
        assertEquals(stateMachine, result);
    }

    /**
     * 测试createProxy方法当apiInterface为空字符串时直接返回stateMachine
     */
    @Test
    public void testCreateProxyWithEmptyApiInterface() throws Exception {
        Object result = StateMachineApiProxy.createProxy(stateMachine, applicationContext, "");
        assertEquals(stateMachine, result);
    }

    /**
     * 测试createProxy方法创建代理对象
     */
    @Test
    public void testCreateProxy() throws Exception {
        // 由于DemoStateMachine现在继承了StateMachine，这里使用自定义的简单测试接口
        // 模拟状态机行为
        when(stateMachine.getCurrentState()).thenReturn("INIT");
        when(stateMachine.execute(anyString(), any(Context.class))).thenReturn("SUCCESS");
        
        // 创建代理对象
        Object proxy = StateMachineApiProxy.createProxy(stateMachine, applicationContext, "com.jameswushanghai.statemachine.core.StateMachineApiProxyTest$TestInterface");
        
        // 验证代理对象不为null
        assertNotNull(proxy);
        
        // 验证代理对象实现了TestInterface接口
        assertTrue(proxy instanceof TestInterface);
        
        // 验证代理对象的方法调用
        TestInterface testProxy = (TestInterface) proxy;
        testProxy.testMethod();
        
        // 验证状态机execute方法被调用
        verify(stateMachine).execute(eq("testMethod"), any(Context.class));
    }

    /**
     * 测试invoke方法处理Object类的方法
     */
    @Test
    public void testInvokeObjectMethod() throws Throwable {
        // 获取Object类的toString方法
        Method toStringMethod = Object.class.getMethod("toString");
        
        // 调用invoke方法
        String result = (String) stateMachineApiProxy.invoke(stateMachineApiProxy, toStringMethod, null);
        
        // 验证结果不为null
        assertNotNull(result);
        // 验证结果包含类名
        assertTrue(result.contains("StateMachineApiProxy"));
    }

    /**
     * 测试invoke方法当状态机未初始化时抛出异常
     */
    @Test
    public void testInvokeWithUninitializedStateMachine() throws Throwable {
        // 模拟状态机未初始化
        when(stateMachine.getCurrentState()).thenReturn(null);
        
        // 获取一个方法（这里使用toString方法，但声明类不是Object）
        Method testMethod = TestInterface.class.getMethod("testMethod");
        
        // 验证抛出IllegalStateException异常
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> stateMachineApiProxy.invoke(new Object(), testMethod, null)
        );
        
        // 验证异常消息
        assertEquals("状态机未初始化，请先调用initialize方法", exception.getMessage());
    }

    /**
     * 测试invoke方法正常执行动作
     */
    @Test
    public void testInvokeNormalExecution() throws Throwable {
        // 模拟状态机已初始化
        when(stateMachine.getCurrentState()).thenReturn("INIT");
        // 模拟execute方法返回SUCCESS状态
        when(stateMachine.execute("testMethod", context)).thenReturn("SUCCESS");
        
        // 获取测试方法
        Method testMethod = TestInterface.class.getMethod("testMethod");
        Object proxy = new Object();
        
        // 调用invoke方法
        Object result = stateMachineApiProxy.invoke(proxy, testMethod, null);
        
        // 验证返回了代理对象自身
        assertEquals(proxy, result);
        
        // 验证状态机execute方法被正确调用
        verify(stateMachine).execute("testMethod", context);
    }

    /**
     * 测试getStateMachine方法返回正确的状态机实例
     */
    @Test
    public void testGetStateMachine() {
        StateMachine retrievedStateMachine = stateMachineApiProxy.getStateMachine();
        assertEquals(stateMachine, retrievedStateMachine);
    }

    /**
     * 用于测试的接口
     */
    public interface TestInterface {
        void testMethod();
    }
    
    /**
     * 测试invoke方法处理带有参数的方法调用
     */
    @Test
    public void testInvokeWithParameters() throws Throwable {
        // 模拟状态机已初始化
        when(stateMachine.getCurrentState()).thenReturn("INIT");
        when(stateMachine.execute("methodWithParams", context)).thenReturn("SUCCESS");
        
        // 创建一个带有参数的测试方法
        Method testMethod = TestInterfaceWithParams.class.getMethod("methodWithParams", String.class, int.class);
        Object proxy = new Object();
        Object[] args = {"test", 123};
        
        // 调用invoke方法
        Object result = stateMachineApiProxy.invoke(proxy, testMethod, args);
        
        // 验证返回了代理对象自身
        assertEquals(proxy, result);
        
        // 验证状态机execute方法被正确调用
        verify(stateMachine).execute("methodWithParams", context);
    }
    
    /**
     * 测试createProxy方法创建多个不同的代理对象
     */
    @Test
    public void testCreateMultipleProxies() throws Exception {
        // 模拟状态机行为
        when(stateMachine.getCurrentState()).thenReturn("INIT");
        when(stateMachine.execute(anyString(), any(Context.class))).thenReturn("SUCCESS");
        
        // 创建第一个代理对象
        Object proxy1 = StateMachineApiProxy.createProxy(stateMachine, applicationContext,
                "com.jameswushanghai.statemachine.api.DemoStateMachine");
        
        // 创建第二个代理对象
        Object proxy2 = StateMachineApiProxy.createProxy(stateMachine, applicationContext,
                "com.jameswushanghai.statemachine.api.DemoStateMachine");
        
        // 验证两个代理对象是不同的实例
        assertNotSame(proxy1, proxy2);
        
        // 验证两个代理对象都是DemoStateMachine类型
        assertTrue(proxy1 instanceof com.jameswushanghai.statemachine.api.DemoStateMachine);
        assertTrue(proxy2 instanceof com.jameswushanghai.statemachine.api.DemoStateMachine);
    }
    
    /**
     * 测试invoke方法处理异常情况
     */
    @Test
    public void testInvokeWithException() throws Throwable {
        // 模拟状态机已初始化
        when(stateMachine.getCurrentState()).thenReturn("INIT");
        // 模拟execute方法抛出异常
        RuntimeException expectedException = new RuntimeException("模拟执行异常");
        doThrow(expectedException).when(stateMachine).execute(anyString(), any(Context.class));
        
        // 获取测试方法
        Method testMethod = TestInterface.class.getMethod("testMethod");
        Object proxy = new Object();
        
        // 调用invoke方法，应该传播异常
        RuntimeException actualException = assertThrows(RuntimeException.class, () -> {
            stateMachineApiProxy.invoke(proxy, testMethod, null);
        });
        
        // 验证抛出的异常是同一个异常对象
        assertSame(expectedException, actualException);
    }
    
    /**
     * 测试createProxy方法在加载无效接口时的异常处理
     */
    @Test
    public void testCreateProxyWithInvalidInterface() {
        // 测试加载无效接口，应该抛出RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            StateMachineApiProxy.createProxy(stateMachine, applicationContext, "non.existent.Interface");
        });
        
        // 验证异常消息包含类名
        assertTrue(exception.getMessage().contains("无法加载API接口类"));
    }
    
    /**
     * 用于测试带参数方法调用的接口
     */
    public interface TestInterfaceWithParams {
        void methodWithParams(String param1, int param2);
    }
}