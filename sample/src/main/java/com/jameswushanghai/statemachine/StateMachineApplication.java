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
 * 状态机示例应用程序
 * 用于演示如何使用状态机框架创建和执行状态机
 */
@SpringBootApplication
public class StateMachineApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(StateMachineApplication.class);
    
    @Autowired
    private StateMachineFactory stateMachineFactory;
    
    public static void main(String[] args) {
        log.info("启动状态机示例应用程序");
        ConfigurableApplicationContext context = SpringApplication.run(StateMachineApplication.class, args);
        SpringApplication.exit(context);
    }
    
    @Override
    public void run(String... args) throws Exception {
        log.info("开始演示状态机的使用");
        
        // 从XML配置创建状态机（这会自动返回API代理对象）
        DemoStateMachine demoStateMachine = (DemoStateMachine) stateMachineFactory.createStateMachineFromXml("classpath:demo-state-machine.xml");
        log.info("状态机创建成功");
        
        // 执行状态机操作
        log.info("执行状态机的start方法");
        demoStateMachine.start();
        
        // 获取原始状态机实例以查看当前状态
        StateMachine stateMachine = stateMachineFactory.getStateMachine("demoMachine");
        log.info("start方法执行后的状态: {}", stateMachine.getCurrentState());
        
        // 执行重试操作
        log.info("执行状态机的retry方法");
        demoStateMachine.retry();
        log.info("retry方法执行后的状态: {}", stateMachine.getCurrentState());
        
        // 演示如何创建和使用上下文
        log.info("演示上下文的使用");
        Context context = new Context();
        context.set("firstExecution", true);
        
        // 直接使用状态机接口执行动作
        stateMachine.initialize("INIT");
        String newState = stateMachine.execute("start", context);
        log.info("直接执行start动作后的状态: {}", newState);
        
        // 演示autoMoveForward功能
        log.info("\n==== 演示autoMoveForward功能 ====");
        
        // 创建使用autoMoveForward功能的状态机
        StateMachine autoMoveMachine = (StateMachine) stateMachineFactory.createStateMachineFromXml("classpath:auto-move-forward-demo.xml");
        log.info("自动前进状态机创建成功");
        
        // 初始化状态机
        autoMoveMachine.initialize("INIT");
        log.info("自动前进状态机初始状态: {}", autoMoveMachine.getCurrentState());
        
        // 执行start动作，由于autoMoveForward="true"，应该会自动执行下一个状态的动作
        log.info("执行start动作，开启自动前进流程");
        Context autoContext = new Context();
        autoContext.set("firstExecution", false); // 设置为不是第一次执行，确保start动作返回SUCCESS
        String finalState = autoMoveMachine.execute("start", autoContext);
        
        // 验证最终状态，由于autoMoveForward功能，应该已经到达COMPLETED状态
        log.info("autoMoveForward流程执行后的最终状态: {}", finalState);
        log.info("autoMoveForward功能演示完成");
        
        log.info("\n状态机示例演示完成");
    }
}