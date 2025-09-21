package com.jameswushanghai.statemachine.model;

import java.util.List;

/**
 * 状态配置模型
 * 表示状态机中的一个状态配置
 */
public class StateConfig {
    
    // 状态名称
    private String name;
    
    // 状态中的动作配置列表
    private List<ActionConfig> actions;
    
    /**
     * 检查状态是否有动作
     * @return 是否有动作
     */
    public boolean hasActions() {
        return actions != null && !actions.isEmpty();
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for actions
    public List<ActionConfig> getActions() {
        return actions;
    }

    public void setActions(List<ActionConfig> actions) {
        this.actions = actions;
    }
    
    /**
     * 根据动作名称查找动作配置
     * @param actionName 动作名称
     * @return 动作配置，如果找不到返回null
     */
    public ActionConfig findAction(String actionName) {
        if (!hasActions() || actionName == null) {
            return null;
        }
        
        for (ActionConfig action : actions) {
            if (actionName.equals(action.getName())) {
                return action;
            }
        }
        
        return null;
    }
}