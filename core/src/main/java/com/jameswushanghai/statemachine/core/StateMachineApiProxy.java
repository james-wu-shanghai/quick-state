package com.jameswushanghai.statemachine.core;

import com.jameswushanghai.statemachine.model.StateMachineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 状态机API代理类
 * 用于创建状态机API接口的代理对象，将方法调用转发到对应的action
 */
public class StateMachineApiProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(StateMachineApiProxy.class);
    
    // 状态机实例
    private final StateMachine stateMachine;
    
    // Spring应用上下文
    private final ApplicationContext applicationContext;
    
    // 上下文对象
    private final Context context;
    
    // 缓存接口类
    private static final Map<String, Class<?>> INTERFACE_CACHE = new ConcurrentHashMap<>();

    /**
     * 构造函数
     * @param stateMachine 状态机实例
     * @param applicationContext Spring应用上下文
     * @param context 上下文对象
     */
    public StateMachineApiProxy(StateMachine stateMachine, ApplicationContext applicationContext, Context context) {
        this.stateMachine = stateMachine;
        this.applicationContext = applicationContext;
        this.context = context;
    }

    /**
     * 创建API代理对象
     * @param stateMachine 状态机实例
     * @param applicationContext Spring应用上下文
     * @param apiInterface API接口类名
     * @return 代理对象
     * @throws Exception 创建异常
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
     * 加载接口类
     * @param apiInterface 接口类名
     * @return 接口类
     * @throws ClassNotFoundException 类未找到异常
     */
    private static Class<?> loadInterfaceClass(String apiInterface) throws ClassNotFoundException {
        return INTERFACE_CACHE.computeIfAbsent(apiInterface, className -> {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("无法加载API接口类: " + className, e);
            }
        });
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果是Object类的方法，直接调用
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        
        // 获取方法名作为动作名
        String actionName = method.getName();
        log.debug("调用状态机API方法: {}", actionName);
        
        // 如果状态机未初始化，抛出异常
        if (stateMachine.getCurrentState() == null) {
            throw new IllegalStateException("状态机未初始化，请先调用initialize方法");
        }
        
        // 执行动作
        String nextState = stateMachine.execute(actionName, context);
        log.debug("状态机从[{}]转换到[{}]", stateMachine.getCurrentState(), nextState);
        
        // 返回代理对象自身以支持链式调用
        return proxy;
    }
    
    /**
     * 获取内部的状态机实例
     * @return 状态机实例
     */
    public StateMachine getStateMachine() {
        return stateMachine;
    }
}