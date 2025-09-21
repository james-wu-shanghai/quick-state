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
     * 从XML文件创建状态机
     * @param configLocation XML配置文件路径
     * @return 状态机实例
     * @throws Exception 创建异常
     */
    public StateMachine createStateMachineFromXml(String configLocation) throws Exception {
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
     * 从XML字符串创建状态机
     * @param xmlString XML配置字符串
     * @return 状态机实例
     * @throws Exception 创建异常
     */
    public StateMachine createStateMachineFromXmlString(String xmlString) throws Exception {
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
     * 根据配置创建状态机
     * @param config 状态机配置
     * @return 状态机实例
     */
    private StateMachine createStateMachine(StateMachineConfig config) {
        DefaultStateMachine stateMachine = new DefaultStateMachine(config, applicationContext);
        stateMachines.put(config.getName(), stateMachine);
        log.info("成功创建状态机[{}]，包含{}个状态", config.getName(), config.getStateCount());
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