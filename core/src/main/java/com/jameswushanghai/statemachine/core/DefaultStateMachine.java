package com.jameswushanghai.statemachine.core;

import com.jameswushanghai.statemachine.model.StateMachineConfig;
import com.jameswushanghai.statemachine.model.StateConfig;
import com.jameswushanghai.statemachine.model.ActionConfig;
import com.jameswushanghai.statemachine.model.NextState;
import org.springframework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认状态机实现
 * 实现StateMachine接口的基本功能
 */
public class DefaultStateMachine implements StateMachine {

    private static final Logger log = LoggerFactory.getLogger(DefaultStateMachine.class);
    
    // 状态机名称
    private String name;
    
    // 当前状态
    private String currentState;
    
    // 状态机配置
    private StateMachineConfig config;
    
    // Spring上下文，用于获取动作实例
    private ApplicationContext applicationContext;
    
    /**
     * 构造函数
     * @param config 状态机配置
     * @param applicationContext Spring上下文
     */
    public DefaultStateMachine(StateMachineConfig config, ApplicationContext applicationContext) {
        this.config = config;
        this.name = config.getName();
        this.applicationContext = applicationContext;
    }

    // Getter and Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for config
    public StateMachineConfig getConfig() {
        return config;
    }

    public void setConfig(StateMachineConfig config) {
        this.config = config;
    }

    // Getter and Setter for applicationContext
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    // Setter for currentState
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getCurrentState() {
        return currentState;
    }
    
    @Override
    public String execute(String actionName, Context context) throws Exception {
        if (currentState == null) {
            throw new IllegalStateException("状态机未初始化，请先调用initialize方法");
        }
        
        // 获取当前状态配置
        StateConfig stateConfig = config.getState(currentState);
        if (stateConfig == null) {
            throw new IllegalStateException("无效的当前状态: " + currentState);
        }
        
        // 查找动作配置
        ActionConfig actionConfig = stateConfig.findAction(actionName);
        if (actionConfig == null) {
            throw new IllegalArgumentException("在状态" + currentState + "中找不到动作: " + actionName);
        }
        
        // 获取动作实例
        Action action = getAction(actionConfig.getRef());
        
        // 执行动作并获取响应码
        String respCode = action.doAction(context);
        log.debug("状态机[{}]在状态[{}]执行动作[{}]，响应码: {}", name, currentState, actionName, respCode);
        
        // 查找下一个状态
        NextState nextState = findNextState(actionConfig, respCode);
        if (nextState != null) {
            // 更新当前状态
            String oldState = currentState;
            currentState = nextState.getState();
            log.debug("状态机[{}]从状态[{}]转换到状态[{}]", name, oldState, currentState);
            
            // 检查是否需要自动执行下一个状态的动作
            if (actionConfig.isAutoMoveForward()) {
                // 获取下一个状态的配置
                StateConfig nextStateConfig = config.getState(currentState);
                if (nextStateConfig != null && nextStateConfig.getActions() != null && nextStateConfig.getActions().size() == 1) {
                    // 只有一个动作，自动执行
                    ActionConfig nextActionConfig = nextStateConfig.getActions().get(0);
                    log.debug("状态机[{}]在状态[{}]自动执行动作[{}]", name, currentState, nextActionConfig.getName());
                    
                    // 递归调用execute方法执行下一个动作
                    return execute(nextActionConfig.getName(), context);
                }
            }
        }
        
        return currentState;
    }
    
    @Override
    public void initialize(String initialState) {
        if (!config.containsState(initialState)) {
            throw new IllegalArgumentException("无效的初始状态: " + initialState);
        }
        
        this.currentState = initialState;
        log.debug("状态机[{}]初始化，初始状态: {}", name, initialState);
    }
    
    @Override
    public boolean isFinalState() {
        // 简单实现：检查当前状态是否没有任何动作
        if (currentState == null) {
            return false;
        }
        
        StateConfig stateConfig = config.getState(currentState);
        return stateConfig == null || !stateConfig.hasActions();
    }
    
    /**
     * 从Spring上下文中获取动作实例
     * @param beanName Spring bean名称
     * @return 动作实例
     */
    private Action getAction(String beanName) {
        if (applicationContext == null) {
            throw new IllegalStateException("Spring上下文未设置，无法获取动作实例");
        }
        
        if (!applicationContext.containsBean(beanName)) {
            throw new IllegalArgumentException("找不到动作的Spring bean: " + beanName);
        }
        
        return applicationContext.getBean(beanName, Action.class);
    }
    
    /**
     * 根据响应码查找下一个状态
     * @param actionConfig 动作配置
     * @param respCode 响应码
     * @return 下一个状态配置
     */
    private NextState findNextState(ActionConfig actionConfig, String respCode) {
        if (actionConfig.getNextStates() == null || actionConfig.getNextStates().isEmpty()) {
            return null;
        }
        
        for (NextState nextState : actionConfig.getNextStates()) {
            if (respCode.equals(nextState.getRespCode())) {
                return nextState;
            }
        }
        
        return null;
    }
}