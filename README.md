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
├── persist/             # 持久化模块，提供状态机配置和状态的数据库持久化能力
│   ├── pom.xml          # 持久化模块的POM文件
│   └── src/
│       ├── main/        # 主要源代码
│       │   ├── java/    # Java源码
│       │   └── resources/ # 配置资源文件
│       └── test/        # 测试代码
├── sample/              # 示例模块，包含状态机使用示例
│   ├── pom.xml          # 示例模块的POM文件
│   └── src/
│       └── main/        # 主要源代码和配置
└── req/                 # 需求文档目录
    ├── 001_init         # 初始需求文档
    ├── 002_state_machine # 状态机核心功能需求
    ├── 003_add_state_machine_testcase # 测试用例需求
    ├── 004_state_machine_api # API需求
    ├── 005_convert2modeule # 模块化需求
    ├── 006_add_sample_module # 示例模块需求
    ├── 007_auto_move_fwd # 自动状态前进需求
    └── 008_db_support   # 数据库支持需求
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

### 示例模块 (sample)
- **作用**: 提供状态机框架的使用示例，帮助开发者快速上手
- **主要内容**: 
  - 完整的Spring Boot应用示例
  - 状态机API接口定义示例 (`DemoStateMachine`)
  - 自定义动作实现示例 (`StartAction`, `RetryAction`)
  - XML状态机配置示例 (`demo-state-machine.xml`)
  - 状态机使用流程演示

### 持久化模块 (persist)
- **作用**: 提供状态机配置和状态的数据库持久化能力
- **主要功能**: 
  - 状态机配置持久化：将状态机XML配置保存到数据库
  - 状态机状态持久化：将状态机当前状态保存到数据库
  - 数据库初始化：从数据库加载状态机配置和状态，初始化状态机实例
  - Spring Data JPA集成：使用JPA进行数据库操作，支持多种数据库系统
  - H2数据库支持：内置支持H2内存数据库，便于开发和测试
- **主要组件**: 
  - `PersistService`: 持久化服务接口，定义持久化操作
  - `PersistServiceImpl`: 持久化服务实现
  - `StateMachineConfig`(实体类): 状态机配置实体
  - `StateMachineState`(实体类): 状态机状态实体
  - `PersistUtils`: 持久化工具类，提供便捷的持久化操作方法

## 功能特点

1. **灵活的状态配置**: 通过XML配置文件定义状态、事件和转换规则
2. **可扩展的动作系统**: 支持自定义动作实现，在状态转换时执行特定业务逻辑
3. **上下文管理**: 提供上下文对象，在状态转换过程中传递和共享数据
4. **工厂模式**: 通过工厂类统一创建和管理状态机实例
5. **API代理**: 提供状态机API代理，简化状态机的使用
6. **持久化支持**: 通过persist模块提供状态机配置和状态的数据库持久化能力
7. **多种数据库支持**: 支持H2、MySQL等多种数据库系统
8. **自动状态前进**: 支持状态机在满足条件时自动向前转换到下一状态

## 技术栈
- Java 17
- Spring Boot 2.7.18
- Spring Data JPA
- TestNG 7.5
- SLF4J 1.7.36
- Log4j2 2.17.2
- Mockito 4.5.1
- H2 Database (默认内置)

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

### 示例模块使用流程

1. **构建并运行示例应用**
   在项目根目录执行以下命令构建项目并运行示例应用：
   
   ```bash
   mvn clean install
   cd sample
   mvn spring-boot:run
   ```

2. **示例状态机结构**
   示例模块中定义了一个简单的状态机，包含以下状态：
   - `INIT`: 初始状态
   - `SUCCESS`: 成功状态
   - `FAIL`: 失败状态

3. **示例API使用**
   示例模块提供了`DemoStateMachine`接口，通过状态机代理实现：
   
   ```java
   // Inject state machine API proxy
   @Autowired
   private DemoStateMachine demoStateMachine;
   
   // Create context and set data
   Context context = new Context();
   context.put("inputData", "test data");
   
   // Call state machine API to execute operation
   String result = demoStateMachine.process(context);
   System.out.println("State machine execution result: " + result);
   ```

### 核心模块使用示例

#### 创建状态机

```java
// Create state machine from XML configuration file
   StateMachineConfig config = StateMachineXmlParser.parse("statemachine/HelloworldStateDefine.xml");
   StateMachine stateMachine = StateMachineFactory.createStateMachine(config);

   // Create context
   Context context = new Context();
   context.put("key", "value");

   // Fire event
   String currentState = stateMachine.getCurrentState();
   stateMachine.fireEvent("EVENT_NAME", context);
   String newState = stateMachine.getCurrentState();
```

#### 定义自定义动作

```java
public class CustomAction implements Action {
    @Override
    public String doAction(Context context) {
        // Execute custom business logic
        String input = context.get("inputKey", String.class);
        // Process data
        String result = processInput(input);
        // Set result to context
        context.put("resultKey", result);
        // Return response code for state transition
        return "SUCCESS_CODE";
    }
    
    private String processInput(String input) {
        // 实现具体的业务逻辑
        return "processed_" + input;
    }
}
```

### 持久化模块使用示例

#### 1. 保存状态机配置到数据库

```java
// 状态机配置XML
String configXml = "<stateMachine name='demoMachine'>...</stateMachine>";

// 保存到数据库
StateMachinePersistUtils.saveConfigToDatabase("demoMachine", configXml);
```

#### 2. 从数据库初始化状态机

```java
// 从数据库初始化状态机
StateMachine stateMachine = StateMachinePersistUtils.initializeFromDatabase("demoMachine");

// 使用状态机
Context context = new Context();
String newState = stateMachine.execute("start", context);
```

#### 3. 保存状态机状态到数据库

```java
// 执行状态机操作后，保存状态到数据库
StateMachinePersistUtils.saveStateToDatabase(stateMachine);
```

#### 4. 检查状态机是否存在

```java
boolean exists = StateMachinePersistUtils.existsStateMachine("demoMachine");
```

#### 5. 删除状态机

```java
StateMachinePersistUtils.deleteStateMachine("demoMachine");
```
```

## 注意事项
- 确保所有状态机配置文件放在 `resources/statemachine/` 目录下
- 自定义动作需要实现 `Action` 接口
- 状态和事件名称必须与配置文件中定义的一致
- 示例模块依赖core模块，请确保core模块已成功构建
- 使用持久化功能时，需要添加persist模块依赖并配置数据库连接
- 默认使用H2内存数据库，可在配置文件中修改为其他数据库

## 未来规划
- 添加更多配置方式（如注解、DSL等）
- 增强状态机监控和日志功能
- 支持分布式状态管理
- 提供更多开箱即用的示例和模板
- 优化持久化模块性能
- 添加可视化状态管理界面

## 提交代码到GitHub

以下是将代码提交到GitHub的基本步骤：

1. **添加修改的文件**
   ```bash
git add .
```

2. **提交更改**
   ```bash
git commit -m "更新README.md文档"
```

3. **推送到远程仓库**
   ```bash
git push origin master
```

> 注意：如果是第一次提交，需要先配置远程仓库地址：
> ```bash
> git remote add origin <your-github-repo-url>
> git push -u origin master
> ```