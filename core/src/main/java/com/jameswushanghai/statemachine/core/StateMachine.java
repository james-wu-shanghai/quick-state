package com.jameswushanghai.statemachine.core;

/**
 * 状态机接口
 * 定义状态机的基本操作
 */
public interface StateMachine {
    
    /**
     * 获取状态机名称
     * @return 状态机名称
     */
    String getName();
    
    /**
     * 获取当前状态
     * @return 当前状态名称
     */
    String getCurrentState();
    
    /**
     * 执行指定动作
     * @param actionName 动作名称
     * @param context 上下文对象
     * @return 执行结果，返回新的状态名称
     * @throws Exception 执行过程中的异常
     */
    String execute(String actionName, Context context) throws Exception;
    
    /**
     * 初始化状态机
     * @param initialState 初始状态名称
     */
    void initialize(String initialState);
    
    /**
     * 检查状态机是否处于终态
     * @return 是否处于终态
     */
    boolean isFinalState();
}