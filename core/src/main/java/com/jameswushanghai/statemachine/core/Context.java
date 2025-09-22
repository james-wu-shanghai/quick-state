package com.jameswushanghai.statemachine.core;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 状态机上下文类
 * 用于在状态机执行过程中存储和传递数据
 */
public class Context {
    
    private static final Logger log = LoggerFactory.getLogger(Context.class);
    
    // 存储上下文数据的Map
    private final Map<String, Object> data = new HashMap<>();
    
    /**
     * 获取上下文中的数据
     * @param key 数据键名
     * @return 数据值
     */
    public Object get(String key) {
        return data.get(key);
    }
    /**
     * 获取上下文中的数据并返回默认值
     * @param key 数据键名
     * @param defaultValue 默认值
     * @return 数据值或默认值
     */
    public Object getOrDefault(String key, Object defaultValue) {
        Object value = data.get(key);
        return (value != null) ? value : defaultValue;
    }

    /**
     * 获取上下文中的数据并进行类型转换
     * @param key 数据键名
     * @param type 目标类型
     * @param <T> 泛型类型
     * @return 转换后的数据值
     * @throws ClassCastException 如果类型转换失败
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        
        if (type.isInstance(value)) {
            return (T) value;
        } else {
            log.warn("无法将值[{}]从类型[{}]转换为[{}]", value, value.getClass().getName(), type.getName());
            throw new ClassCastException("Cannot cast " + value.getClass().getName() + " to " + type.getName());
        }
    }
    
    /**
     * 设置上下文中的数据
     * @param key 数据键名
     * @param value 数据值
     * @return 上下文对象本身，用于链式调用
     */
    public Context set(String key, Object value) {
        data.put(key, value);
        return this;
    }
    
    /**
     * 检查上下文中是否包含指定键的数据
     * @param key 数据键名
     * @return 是否包含
     */
    public boolean contains(String key) {
        return data.containsKey(key);
    }
    
    /**
     * 清除上下文中的所有数据
     */
    public void clear() {
        data.clear();
    }
    
    public boolean isEmpty() {
        return data.isEmpty();
    }
}