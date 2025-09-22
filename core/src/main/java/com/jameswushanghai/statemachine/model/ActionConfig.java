package com.jameswushanghai.statemachine.model;

import java.util.List;

/**
 * 动作配置模型
 * 表示在状态中定义的动作配置
 */
public class ActionConfig {
    
    // 动作名称
    private String name;
    
    // 引用的Spring bean名称
    private String ref;
    
    // 下一个可能的状态列表
    private List<NextState> nextStates;
    
    // 是否自动前进到下一个状态
    private boolean autoMoveForward = false;

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for ref
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    // Getter and Setter for nextStates
    public List<NextState> getNextStates() {
        return nextStates;
    }

    public void setNextStates(List<NextState> nextStates) {
        this.nextStates = nextStates;
    }
    
    // Getter and Setter for autoMoveForward
    public boolean isAutoMoveForward() {
        return autoMoveForward;
    }
    
    public void setAutoMoveForward(boolean autoMoveForward) {
        this.autoMoveForward = autoMoveForward;
    }
}