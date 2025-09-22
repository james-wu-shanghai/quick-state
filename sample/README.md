# State Machine Sample Module

本模块提供了一个基于core模块的状态机使用示例，旨在指导新手如何使用状态机框架。

## 模块结构

```
src/main/java/com/jameswushanghai/statemachine/
├── StateMachineApplication.java  # 应用程序入口
├── api/
│   └── DemoStateMachine.java     # 状态机API接口
└── action/
    ├── StartAction.java          # 开始动作实现
    └── RetryAction.java          # 重试动作实现
src/main/resources/
├── demo-state-machine.xml        # 状态机XML配置
└── application.properties        # Spring Boot配置
```

## 功能说明

1. **DemoStateMachine接口**：定义了状态机的两个操作方法：start()和retry()

2. **StartAction类**：实现了start动作的逻辑，第一次执行返回失败，第二次返回成功

3. **RetryAction类**：实现了retry动作的逻辑，记录重试次数并返回成功

4. **StateMachineApplication类**：Spring Boot应用程序入口，演示了如何创建和使用状态机

5. **XML配置文件**：定义了状态机的结构、状态、动作和转换规则

## 状态机流程

- **初始状态(INIT)**：可以执行start动作
  - 执行成功：转换到SUCCESS状态
  - 执行失败：转换到FAIL状态
- **失败状态(FAIL)**：可以执行retry动作
  - 执行成功：转换到SUCCESS状态
- **成功状态(SUCCESS)**：终态，没有定义任何动作

## 如何运行

1. 确保core模块已经构建完成
2. 在根目录执行`mvn clean install`构建项目
3. 运行sample模块的StateMachineApplication类

## 使用示例

```java
// 从XML配置创建状态机
DemoStateMachine demoStateMachine = (DemoStateMachine) stateMachineFactory.createStateMachineFromXml("classpath:demo-state-machine.xml");

// 执行状态机操作（链式调用）
demoStateMachine.start().retry();

// 或者使用原始状态机接口
StateMachine stateMachine = stateMachineFactory.getStateMachine("demoMachine");
stateMachine.initialize("INIT");
Context context = new Context();
String newState = stateMachine.execute("start", context);
```

## 注意事项

1. 确保正确配置XML文件中的action引用与Spring Bean名称一致
2. 状态机名称在XML配置中定义，用于后续获取状态机实例
3. API接口中的方法名应与XML配置中的action名称对应