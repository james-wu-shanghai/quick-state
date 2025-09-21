package com.jameswushanghai.statemachine.api;

/**
 * 演示状态机API接口
 * 用于测试状态机的API功能
 */
public interface DemoStateMachine {
    /**
     * 开始状态机执行
     * @return 状态机API代理对象，支持链式调用
     */
    DemoStateMachine start();
    
    /**
     * 重试操作
     * @return 状态机API代理对象，支持链式调用
     */
    DemoStateMachine retry();
}