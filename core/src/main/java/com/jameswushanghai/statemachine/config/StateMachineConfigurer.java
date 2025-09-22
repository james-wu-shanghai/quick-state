package com.jameswushanghai.statemachine.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * State machine configuration class
 * Configures component scanning and other settings for the state machine framework
 */
@Configuration
@ComponentScan({
    "com.jameswushanghai.statemachine.core",
    "com.jameswushanghai.statemachine.parser",
    "com.jameswushanghai.statemachine.api"
})
public class StateMachineConfigurer {
    
    // Additional configurations for the state machine framework can be added here
    // For example: custom XML parser configuration, advanced state machine factory configuration, etc.
}