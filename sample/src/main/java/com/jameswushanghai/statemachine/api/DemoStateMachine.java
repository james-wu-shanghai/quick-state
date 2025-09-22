package com.jameswushanghai.statemachine.api;

/**
 * 演示状态机API接口
 * 提供start和retry方法用于控制状态机的执行流程
 */
public interface DemoStateMachine {
    /**
     * 启动状态机
     * @return 当前状态机实例，支持链式调用
     */
    DemoStateMachine start();
    
    /**
     * 重试状态机
     * @return 当前状态机实例，支持链式调用
     */
    DemoStateMachine retry();
}