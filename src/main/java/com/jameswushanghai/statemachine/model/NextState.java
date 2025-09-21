package com.jameswushanghai.statemachine.model;

/**
 * 下一个状态模型
 * 表示从当前状态执行动作后可能转换到的下一个状态
 */
public class NextState {
    
    // 响应代码，与动作返回值匹配
    private String respCode;
    
    // 目标状态名称
    private String state;

    // Getter and Setter for respCode
    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    // Getter and Setter for state
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}