package com.jameswushanghai.statemachine.persist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * State Machine Persistence Configuration Class
 * Configure component scanning and MyBatis Mapper for state machine persistence module
 */
@Configuration
@ComponentScan("com.jameswushanghai.statemachine.persist")
@MapperScan("com.jameswushanghai.statemachine.persist.mapper")
public class StateMachinePersistConfigurer {
    
    // Other configurations for state machine persistence module can be added here
}