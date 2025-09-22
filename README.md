# Quick-State 项目说明

## 项目简介
Quick-State 是一个轻量级的状态机框架，用于简化状态驱动的业务流程管理。该框架提供了灵活的状态转换配置、事件处理和动作执行机制，帮助开发者更优雅地处理复杂的业务流程。

## 项目结构
项目采用 Maven 多模块结构组织，主要包含以下模块：

```
quick-state/
├── pom.xml              # 主POM文件，管理所有模块的版本依赖
├── core/                # 核心模块，包含状态机的主要实现
│   ├── pom.xml          # 核心模块的POM文件
│   └── src/
│       ├── main/        # 主要源代码
│       │   ├── java/    # Java源码
│       │   └── resources/ # 配置资源文件
│       └── test/        # 测试代码
└── req/                 # 需求文档目录
    ├── 001_init         # 初始需求文档
    ├── 002_state_machine # 状态机核心功能需求
    ├── 003_add_state_machine_testcase # 测试用例需求
    ├── 004_state_machine_api # API需求
    └── 005_convert2modeule # 模块化需求
```

## 模块说明

### 主模块 (quick-state)
- **作用**: 作为父模块，负责管理所有子模块的版本依赖和共享配置
- **主要职责**: 
  - 定义项目的基本信息和版本
  - 管理所有依赖的版本号
  - 提供共享的插件配置
  - 聚合所有子模块

### 核心模块 (core)
- **作用**: 实现状态机的核心功能
- **主要组件**: 
  - `StateMachine`: 状态机核心接口，定义状态转换和事件处理的基本操作
  - `DefaultStateMachine`: 状态机的默认实现
  - `StateMachineFactory`: 用于创建和管理状态机实例
  - `Context`: 状态机执行的上下文对象，存储状态和数据
  - `Action`: 状态转换时执行的动作接口
  - `StateMachineConfig`: 状态机配置模型
  - `StateMachineXmlParser`: XML配置解析器

## 功能特点

1. **灵活的状态配置**: 通过XML配置文件定义状态、事件和转换规则
2. **可扩展的动作系统**: 支持自定义动作实现，在状态转换时执行特定业务逻辑
3. **上下文管理**: 提供上下文对象，在状态转换过程中传递和共享数据
4. **工厂模式**: 通过工厂类统一创建和管理状态机实例
5. **API代理**: 提供状态机API代理，简化状态机的使用

## 技术栈
- Java 17
- Spring Boot 2.7.18
- TestNG 7.5
- SLF4J 1.7.36
- Log4j2 2.17.2
- Mockito 4.5.1

## 构建和运行

### 构建项目
在项目根目录执行以下命令构建整个项目：

```bash
mvn clean install
```

### 运行测试
在项目根目录执行以下命令运行所有测试：

```bash
mvn test
```

## 使用示例

### 创建状态机

```java
// 从XML配置文件创建状态机
StateMachineConfig config = StateMachineXmlParser.parse("statemachine/HelloworldStateDefine.xml");
StateMachine stateMachine = StateMachineFactory.createStateMachine(config);

// 创建上下文
Context context = new Context();
context.put("key", "value");

// 触发事件
String currentState = stateMachine.getCurrentState();
stateMachine.fireEvent("EVENT_NAME", context);
String newState = stateMachine.getCurrentState();
```

## 注意事项
- 确保所有状态机配置文件放在 `resources/statemachine/` 目录下
- 自定义动作需要实现 `Action` 接口
- 状态和事件名称必须与配置文件中定义的一致

## 未来规划
- 添加更多配置方式（如注解、DSL等）
- 增强状态机监控和日志功能
- 支持分布式状态管理
- 提供更多开箱即用的示例和模板