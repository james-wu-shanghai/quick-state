package com.jameswushanghai.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.jameswushanghai.statemachine.api.DemoStateMachine;
import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.core.StateMachine;
import com.jameswushanghai.statemachine.core.StateMachineFactory;

/**
 * State machine sample application
 * Demonstrates how to create and execute state machines using the state machine framework
 */
@SpringBootApplication
public class StateMachineApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(StateMachineApplication.class);
    
    @Autowired
    private StateMachineFactory stateMachineFactory;
    
    public static void main(String[] args) {
        log.info("Starting state machine sample application");
        ConfigurableApplicationContext context = SpringApplication.run(StateMachineApplication.class, args);
        SpringApplication.exit(context);
    }
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting demonstration of state machine usage");
        
        // Create state machine from XML configuration (this automatically returns API proxy object)
        DemoStateMachine demoStateMachine = (DemoStateMachine) stateMachineFactory.createStateMachineFromXml("classpath:demo-state-machine.xml");
        log.info("State machine created successfully");
        
        // Execute state machine operations
        log.info("Executing start method of state machine");
        demoStateMachine.start();
        
        // Get original state machine instance to view current state
        StateMachine stateMachine = stateMachineFactory.getStateMachine("demoMachine");
        log.info("State after executing start method: {}", stateMachine.getCurrentState());
        
        // Execute retry operation
        log.info("Executing retry method of state machine");
        demoStateMachine.retry();
        log.info("State after executing retry method: {}", stateMachine.getCurrentState());
        
        // Demonstrate how to create and use context
        log.info("Demonstrating context usage");
        Context context = new Context();
        context.set("firstExecution", true);
        
        // Directly use state machine interface to execute action
        stateMachine.initialize("INIT");
        String newState = stateMachine.execute("start", context);
        log.info("State after directly executing start action: {}", newState);
        
        // Demonstrate autoMoveForward functionality
        log.info("\n==== Demonstrating autoMoveForward functionality ====");
        
        // Create state machine with autoMoveForward functionality
        StateMachine autoMoveMachine = (StateMachine) stateMachineFactory.createStateMachineFromXml("classpath:auto-move-forward-demo.xml");
        log.info("Auto move forward state machine created successfully");
        
        // Initialize state machine
        autoMoveMachine.initialize("INIT");
        log.info("Initial state of auto move forward state machine: {}", autoMoveMachine.getCurrentState());
        
        // Execute start action, due to autoMoveForward="true", it should automatically execute next state's actions
        log.info("Executing start action to begin auto forward process");
        Context autoContext = new Context();
        autoContext.set("firstExecution", false); // Set to not first execution to ensure start action returns SUCCESS
        String finalState = autoMoveMachine.execute("start", autoContext);
        
        // Verify final state, due to autoMoveForward functionality, it should have reached COMPLETED state
        log.info("Final state after autoMoveForward process: {}", finalState);
        log.info("AutoMoveForward functionality demonstration completed");
        
        log.info("\nState machine sample demonstration completed");
    }
}