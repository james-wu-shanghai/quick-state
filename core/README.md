# Quick-State Core Module

## Module Introduction

The core module is the foundation of the Quick-State framework, providing the essential implementation of the state machine functionality. This module defines the core interfaces, default implementations, configuration parsing, and factory pattern for creating and managing state machine instances.

## Features

1. **Core State Machine Interface**: Provides the fundamental operations for state transitions and event handling
2. **Default Implementation**: Complete implementation of the state machine logic
3. **Factory Pattern**: Centralized creation and management of state machine instances
4. **XML Configuration Support**: Flexible configuration of states, actions, and transitions through XML
5. **API Proxy**: Simplified usage of state machines through API proxies
6. **Context Management**: Mechanism for passing and sharing data during state transitions
7. **Action System**: Extensible system for implementing custom business logic during state transitions

## Module Structure

```
src/main/java/com/jameswushanghai/statemachine/core/
├── StateMachine.java              # Core state machine interface
├── DefaultStateMachine.java       # Default implementation of StateMachine
├── StateMachineFactory.java       # Factory class for creating state machines
├── StateMachineApiProxy.java      # Proxy for state machine API
├── Context.java                   # Context object for data sharing
├── Action.java                    # Action interface for custom logic
├── parser/                        # Parser package
│   └── StateMachineXmlParser.java # XML configuration parser
src/main/java/com/jameswushanghai/statemachine/model/
├── StateMachineConfig.java        # State machine configuration model
├── StateConfig.java               # State configuration model
├── ActionConfig.java              # Action configuration model
└── NextState.java                 # Next state configuration model
src/main/resources/                # Resource files
```

## Core Components

### StateMachine Interface
The primary interface defining the operations that can be performed on a state machine.

```java
public interface StateMachine {
    String getName();
    String getCurrentState();
    String execute(String actionName, Context context) throws Exception;
    void initialize(String initialState);
    boolean isFinalState();
}
```

### DefaultStateMachine Class
The default implementation of the StateMachine interface, providing the core logic for state transitions and action execution.

Key features:
- State management and transitions
- Action execution
- Response code handling
- Auto-forwarding between states

### StateMachineFactory Class
A factory class responsible for creating and managing state machine instances.

Main methods:
- `createStateMachineFromXml(String configLocation)`: Creates a state machine from an XML configuration file
- `createStateMachineFromXmlString(String xmlString)`: Creates a state machine from an XML string
- `getStateMachine(String name)`: Retrieves a state machine by name
- `getStateMachineApi(String name, Class<T> apiType)`: Retrieves a state machine API proxy

### StateMachineApiProxy Class
A proxy class that enables the use of custom interfaces to interact with state machines, simplifying their usage.

### Context Class
A context object used to pass data between states and actions during state machine execution.

### Action Interface
An interface for implementing custom business logic that is executed during state transitions.

```java
public interface Action {
    String doAction(Context context);
}
```

## XML Configuration

State machines can be configured using XML files. Here's an example of a simple state machine configuration:

```xml
<stateMachine name="demoMachine">
    <states>
        <state name="INIT">
            <actions>
                <action name="start" ref="startAction">
                    <nextStates>
                        <nextState respCode="SUCCESS" state="SUCCESS"/>
                        <nextState respCode="FAIL" state="FAIL"/>
                    </nextStates>
                </action>
            </actions>
        </state>
        <state name="FAIL">
            <actions>
                <action name="retry" ref="retryAction">
                    <nextStates>
                        <nextState respCode="SUCCESS" state="SUCCESS"/>
                    </nextStates>
                </action>
            </actions>
        </state>
        <state name="SUCCESS">
            <!-- Final state with no actions -->
        </state>
    </states>
</stateMachine>
```

## Usage Examples

### Basic Usage

```java
// Create a state machine from XML configuration
StateMachine stateMachine = stateMachineFactory.createStateMachineInterfaceFromXml("classpath:demo-state-machine.xml");

// Initialize the state machine
stateMachine.initialize("INIT");

// Execute an action
Context context = new Context();
String newState = stateMachine.execute("start", context);

// Check if in final state
boolean isFinal = stateMachine.isFinalState();
```

### Using API Proxy

```java
// Define an API interface
public interface DemoStateMachine {
    String start();
    String retry();
}

// Create a state machine with API proxy
DemoStateMachine demoStateMachine = (DemoStateMachine) stateMachineFactory.createStateMachineFromXml("classpath:demo-state-machine.xml");

// Use the API proxy (method calls correspond to actions)
demoStateMachine.start();
demoStateMachine.retry();
```

### Creating Custom Actions

```java
@Component("customAction")
public class CustomAction implements Action {
    @Override
    public String doAction(Context context) {
        // Implement custom business logic
        // Return a response code that determines the next state
        return "SUCCESS";
    }
}
```

## Dependencies

- Java 17
- Spring Boot 2.7.18
- SLF4J 1.7.36
- Log4j2 2.17.2

## Building the Module

To build the core module, run the following command from the project root directory:

```bash
mvn clean install -pl core -am
```

## Testing

To run the tests for the core module, use:

```bash
mvn test -pl core
```