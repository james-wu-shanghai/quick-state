# Quick-State Persist Module

## Module Introduction

The persist module is part of the Quick-State framework, providing database persistence capabilities for state machine configurations and states. This module allows state machines to read initialization data from the database and save intermediate states to the database, enabling persistent storage and recovery of state machines.

## Features

1. **State Machine Configuration Persistence**: Save state machine XML configurations to the database
2. **State Machine State Persistence**: Save current state of state machines to the database
3. **Database Initialization**: Load state machine configurations and states from the database to initialize state machine instances
4. **Spring Data JPA Integration**: Uses JPA for database operations, supporting multiple database systems
5. **H2 Database Support**: Built-in support for H2 in-memory database for development and testing

## Module Structure

```
src/main/java/com/jameswushanghai/statemachine/persist/
├── config/                               # Configuration package
│   └── PersistAutoConfiguration.java     # Auto-configuration class
├── entity/                               # Entity package
│   ├── StateMachineConfig.java           # State machine config entity
│   └── StateMachineState.java            # State machine state entity
├── service/                              # Service layer
│   ├── PersistService.java               # Persistence service interface
│   └── PersistServiceImpl.java           # Persistence service implementation
└── util/                                 # Utility package
    └── PersistUtils.java                 # Persistence utility class
src/main/resources/
├── application.properties                # Spring Boot configuration file
└── schema/                               # Database scripts
    └── schema.sql                        # Table structure initialization script
```

## Database Schema

This module uses Spring Data JPA for database operations. It uses a table named `state_machine` to store state machine configurations and state information. The table schema is as follows:

| Column Name | Data Type | Description |
|-------------|-----------|-------------|
| id | BIGINT | Primary key, auto-increment |
| name | VARCHAR(255) | State machine name, uniquely identifies a state machine instance |
| config | LONGVARCHAR | State machine configuration XML string |
| current_state | VARCHAR(100) | Current state of the state machine |
| create_time | TIMESTAMP | Creation time |
| update_time | TIMESTAMP | Update time |

## Usage

### 1. Adding Dependencies

Add the persist module dependency to your project's pom.xml file:

```xml
<dependency>
    <groupId>com.jameswushanghai</groupId>
    <artifactId>state.machine.persist</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Saving State Machine Configuration to Database

```java
// State machine configuration XML
String configXml = "<stateMachine name='demoMachine'>...</stateMachine>";

// Save to database
StateMachinePersistUtils.saveConfigToDatabase("demoMachine", configXml);
```

### 3. Initializing State Machine from Database

```java
// Initialize state machine from database
StateMachine stateMachine = StateMachinePersistUtils.initializeFromDatabase("demoMachine");

// Use state machine
Context context = new Context();
String newState = stateMachine.execute("start", context);
```

### 4. Saving State Machine State to Database

```java
// Save state to database after executing state machine operations
StateMachinePersistUtils.saveStateToDatabase(stateMachine);
```

### 5. Checking if State Machine Exists

```java
boolean exists = StateMachinePersistUtils.existsStateMachine("demoMachine");
```

### 6. Deleting State Machine

```java
StateMachinePersistUtils.deleteStateMachine("demoMachine");
```

## Configuration

This module default uses H2 in-memory database, configuration information is in `application.properties` file:

```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:statemachine_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
To use other databases, simply modify the database configuration in the `application.properties` file.

## Notes

1. Ensure that state machine names are unique in the database
2. When saving state machine configurations, the state machine name in the XML configuration must match the machineName parameter
3. When initializing a state machine from the database, ensure that the configuration for that state machine already exists
4. Database interactions are handled in the persist module, but the actual state machine execution depends on the core module implementation
5. H2 database defaults to in-memory mode, data will be lost after application restart. For persistent storage, modify the database configuration to file mode
