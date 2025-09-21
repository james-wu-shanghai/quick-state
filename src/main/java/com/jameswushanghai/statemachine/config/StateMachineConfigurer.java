package com.jameswushanghai.statemachine.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 状态机配置类
 * 配置状态机框架的组件扫描和其他配置
 */
@Configuration
@ComponentScan({
    "com.jameswushanghai.statemachine.core",
    "com.jameswushanghai.statemachine.parser",
    "com.jameswushanghai.statemachine.example"
})
public class StateMachineConfigurer {
    
    // 这里可以添加状态机框架的其他配置
    // 例如：自定义的XML解析器配置、状态机工厂的高级配置等
}