package com.jameswushanghai.statemachine.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * State machine API proxy class
 * Used to create proxy objects for state machine API interfaces and forward method calls to corresponding actions
 */
public class StateMachineApiProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(StateMachineApiProxy.class);
    
    // State machine instance
    private final StateMachine stateMachine;
    
    // Spring application context
    private final ApplicationContext applicationContext;
    
    // Context object
    private final Context context;
    
    // Interface class cache
    private static final Map<String, Class<?>> INTERFACE_CACHE = new ConcurrentHashMap<>();

    /**
     * Constructor
     * @param stateMachine State machine instance
     * @param applicationContext Spring application context
     * @param context Context object
     */
    public StateMachineApiProxy(StateMachine stateMachine, ApplicationContext applicationContext, Context context) {
        this.stateMachine = stateMachine;
        this.applicationContext = applicationContext;
        this.context = context;
    }

    /**
     * Create API proxy object
     * @param stateMachine State machine instance
     * @param applicationContext Spring application context
     * @param apiInterface API interface class name
     * @return Proxy object
     * @throws Exception Creation exception
     */
    public static Object createProxy(StateMachine stateMachine, ApplicationContext applicationContext, String apiInterface) throws Exception {
        if (apiInterface == null || apiInterface.isEmpty()) {
            return stateMachine;
        }
        
        // 加载接口类
        Class<?> interfaceClass = loadInterfaceClass(apiInterface);
        
        // 创建上下文对象
        Context context = new Context();
        
        // 创建代理对象
        StateMachineApiProxy handler = new StateMachineApiProxy(stateMachine, applicationContext, context);
        return Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, 
                handler
        );
    }

    /**
     * Load interface class
     * @param apiInterface Interface class name
     * @return Interface class
     * @throws ClassNotFoundException Class not found exception
     */
    private static Class<?> loadInterfaceClass(String apiInterface) throws ClassNotFoundException {
        return INTERFACE_CACHE.computeIfAbsent(apiInterface, className -> {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to load API interface class: " + className, e);
            }
        });
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果是Object类的方法，直接调用
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        
        // 检查方法是否是从StateMachine接口继承的
        if (StateMachine.class.isAssignableFrom(method.getDeclaringClass())) {
            // 直接调用stateMachine对象的相应方法
            log.debug("调用StateMachine接口方法: {}", method.getName());
            return method.invoke(stateMachine, args);
        }
        
        // 获取方法名作为动作名
        String actionName = method.getName();
        log.debug("调用状态机API方法: {}", actionName);
        
        // If state machine is not initialized, throw exception
        if (stateMachine.getCurrentState() == null) {
            throw new IllegalStateException("State machine not initialized, please call initialize method first");
        }
        
        // 执行动作
        String nextState = stateMachine.execute(actionName, context);
        log.debug("状态机从[{}]转换到[{}]", stateMachine.getCurrentState(), nextState);
        
        // 返回代理对象自身以支持链式调用
        return proxy;
    }
    
    /**
     * Get the internal state machine instance
     * @return State machine instance
     */
    public StateMachine getStateMachine() {
        return stateMachine;
    }
}