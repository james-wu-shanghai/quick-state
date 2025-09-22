# Quick-State Sample Module

## Module Introduction

This module provides a complete example of using the Quick-State framework based on the core module. It aims to guide new developers on how to use the state machine framework effectively.

## Module Structure

```
src/main/java/com/jameswushanghai/statemachine/
├── StateMachineApplication.java  # Application entry point
├── api/
│   └── DemoStateMachine.java     # State machine API interface
└── action/
    ├── StartAction.java          # Start action implementation
    └── RetryAction.java          # Retry action implementation
src/main/resources/
├── demo-state-machine.xml        # State machine XML configuration
└── application.properties        # Spring Boot configuration
```

## Features

1. **DemoStateMachine Interface**: Defines two operation methods for the state machine: start() and retry()

2. **StartAction Class**: Implements the logic for the start action, returns failure on first execution and success on second execution

3. **RetryAction Class**: Implements the logic for the retry action, records retry count and returns success

4. **StateMachineApplication Class**: Spring Boot application entry point that demonstrates how to create and use state machines

5. **XML Configuration File**: Defines the state machine structure, states, actions, and transition rules

## State Machine Flow

- **Initial State (INIT)**: Can execute the start action
  - On success: Transition to SUCCESS state
  - On failure: Transition to FAIL state
- **Failure State (FAIL)**: Can execute the retry action
  - On success: Transition to SUCCESS state
- **Success State (SUCCESS)**: Final state, no actions defined

## How to Run

1. Ensure the core module has been built successfully
2. Execute `mvn clean install` in the root directory to build the project
3. Run the StateMachineApplication class in the sample module

## Usage Examples

```java
// Create state machine from XML configuration
DemoStateMachine demoStateMachine = (DemoStateMachine) stateMachineFactory.createStateMachineFromXml("classpath:demo-state-machine.xml");

// Execute state machine operations (chain call)
demoStateMachine.start().retry();

// Or use the original state machine interface
StateMachine stateMachine = stateMachineFactory.getStateMachine("demoMachine");
stateMachine.initialize("INIT");
Context context = new Context();
String newState = stateMachine.execute("start", context);
```

## Notes

1. Ensure that the action references in the XML file are correctly configured to match the Spring Bean names
2. The state machine name is defined in the XML configuration and is used to retrieve the state machine instance later
3. The method names in the API interface should correspond to the action names in the XML configuration