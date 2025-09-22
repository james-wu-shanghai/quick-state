package com.jameswushanghai.statemachine.core;

import com.jameswushanghai.statemachine.model.StateMachineConfig;
import com.jameswushanghai.statemachine.parser.StateMachineXmlParser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 状态机工厂
 * 用于创建和管理状态机实例
 */
@Component
public class StateMachineFactory implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(StateMachineFactory.class);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    // 存储已创建的状态机实例
    private final Map<String, StateMachine> stateMachines = new HashMap<>();
    
    // XML解析器
    private final StateMachineXmlParser parser = new StateMachineXmlParser();
    
    /**
     * 根据名称获取状态机实例
     * @param name 状态机名称
     * @return 状态机实例
     */
    public StateMachine getStateMachine(String name) {
        StateMachine stateMachine = stateMachines.get(name);
        if (stateMachine == null) {
            throw new IllegalArgumentException("找不到状态机: " + name);
        }
        return stateMachine;
    }
    
    /**
     * 根据名称获取状态机API代理对象
     * @param name 状态机名称
     * @param apiType API接口类型
     * @param <T> 泛型类型
     * @return API代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getStateMachineApi(String name, Class<T> apiType) {
        StateMachine stateMachine = getStateMachine(name);
        StateMachineConfig config = ((DefaultStateMachine) stateMachine).getConfig();
        
        if (apiType == null) {
            throw new IllegalArgumentException("API接口类型不能为空");
        }
        
        String apiInterface = config.getApiInterface();
        if (apiInterface == null || !apiInterface.equals(apiType.getName())) {
            throw new IllegalArgumentException("状态机[" + name + "]的API接口类型不匹配");
        }
        
        try {
            return (T) StateMachineApiProxy.createProxy(stateMachine, applicationContext, apiInterface);
        } catch (Exception e) {
            log.error("创建状态机API代理失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建状态机API代理失败", e);
        }
    }
    
    /**
     * 从XML文件创建状态机
     * @param configLocation XML配置文件路径
     * @return 状态机实例或API代理对象
     * @throws Exception 创建异常
     */
    public Object createStateMachineFromXml(String configLocation) throws Exception {
        try {
            Resource resource = resourceLoader.getResource(configLocation);
            if (!resource.exists()) {
                throw new IOException("配置文件不存在: " + configLocation);
            }
            
            StateMachineConfig config = parser.parseFromInputStream(resource.getInputStream());
            return createStateMachine(config);
        } catch (Exception e) {
            log.error("从XML文件创建状态机失败: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 从XML文件创建状态机并返回StateMachine接口
     * @param configLocation XML配置文件路径
     * @return StateMachine接口实例
     * @throws Exception 创建异常
     */
    public StateMachine createStateMachineInterfaceFromXml(String configLocation) throws Exception {
        Object machine = createStateMachineFromXml(configLocation);
        if (machine instanceof StateMachine) {
            return (StateMachine) machine;
        }
        // 如果返回的是代理对象，从内部获取StateMachine实例
        return ((StateMachineApiProxy)Proxy.getInvocationHandler(machine)).getStateMachine();
    }
    
    /**
     * 从XML字符串创建状态机
     * @param xmlString XML配置字符串
     * @return 状态机实例或API代理对象
     * @throws Exception 创建异常
     */
    public Object createStateMachineFromXmlString(String xmlString) throws Exception {
        try {
            if (!StringUtils.hasText(xmlString)) {
                throw new IllegalArgumentException("XML字符串不能为空");
            }
            
            StateMachineConfig config = parser.parseFromString(xmlString);
            return createStateMachine(config);
        } catch (Exception e) {
            log.error("从XML字符串创建状态机失败: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 从XML字符串创建状态机并返回StateMachine接口
     * @param xmlString XML配置字符串
     * @return StateMachine接口实例
     * @throws Exception 创建异常
     */
    public StateMachine createStateMachineInterfaceFromXmlString(String xmlString) throws Exception {
        Object machine = createStateMachineFromXmlString(xmlString);
        if (machine instanceof StateMachine) {
            return (StateMachine) machine;
        }
        // 如果返回的是代理对象，从内部获取StateMachine实例
        return ((StateMachineApiProxy)Proxy.getInvocationHandler(machine)).getStateMachine();
    }
    
    /**
     * 根据配置创建状态机
     * @param config 状态机配置
     * @return 状态机实例或API代理对象
     */
    private Object createStateMachine(StateMachineConfig config) {
        DefaultStateMachine stateMachine = new DefaultStateMachine(config, applicationContext);
        stateMachines.put(config.getName(), stateMachine);
        log.info("成功创建状态机[{}]，包含{}个状态", config.getName(), config.getStateCount());
        
        // 如果配置了API接口，返回代理对象
        String apiInterface = config.getApiInterface();
        if (apiInterface != null && !apiInterface.isEmpty()) {
            try {
                return StateMachineApiProxy.createProxy(stateMachine, applicationContext, apiInterface);
            } catch (Exception e) {
                log.error("创建状态机API代理失败: {}", e.getMessage(), e);
                // 如果创建代理失败，返回原始状态机
                return stateMachine;
            }
        }
        
        // 否则返回原始状态机
        return stateMachine;
    }
    
    /**
     * 初始化后自动扫描并加载状态机配置
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 这里可以实现自动扫描和加载状态机配置的逻辑
        log.info("状态机工厂初始化完成");
    }
}