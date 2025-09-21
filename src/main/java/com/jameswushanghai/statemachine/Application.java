package com.jameswushanghai.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring Boot应用程序主类
 */
public class Application {
    
    public static void main(String[] args) {
        // 启动Spring Boot应用程序
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

    }
}