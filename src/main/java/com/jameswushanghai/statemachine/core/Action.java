package com.jameswushanghai.statemachine.core;

/**
 * 状态机动作接口
 * 所有具体的动作类都需要实现此接口
 */
public interface Action {
    
    /**
     * 执行动作
     * @param context 上下文对象，包含状态机执行过程中的数据
     * @return 响应代码，用于决定下一个状态
     */
    String doAction(Context context);
}