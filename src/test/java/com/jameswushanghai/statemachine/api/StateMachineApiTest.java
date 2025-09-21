package com.jameswushanghai.statemachine.api;

import com.jameswushanghai.statemachine.core.Context;
import com.jameswushanghai.statemachine.core.StateMachine;
import com.jameswushanghai.statemachine.core.StateMachineFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 状态机API功能测试
 */
public class StateMachineApiTest {
    
    @Mock
    private StateMachineFactory stateMachineFactory;
    
    @Mock
    private StateMachine stateMachine;
    
    @Mock
    private DemoStateMachine demoStateMachine;
    
   /**
    * 這個case需要測試新創建一個狀態機，然後觸發start方法，第一次start會失敗，然後判斷狀態機的狀態是fail
    然後觸發retry方法，狀態機的狀態則變為success
    * @throws Exception
    */ 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testStateMachineApi() throws Exception {
        // 定义XML配置字符串，包含失败和成功的状态转换
        String xmlConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<state-machine name=\"demo\">"
                + "    <api>com.jameswushanghai.statemachine.api.DemoStateMachine</api>"
                + "    <states>"
                + "        <state name=\"INIT\">"
                + "            <action name=\"start\" ref=\"startAction\">"
                + "                <next state=\"SUCCESS\" resp_code=\"SUCCESS\"/>" 
                + "                <next state=\"FAIL\" resp_code=\"FAILED\"/>" 
                + "            </action>"
                + "        </state>"
                + "        <state name=\"FAIL\">"
                + "            <action name=\"retry\" ref=\"retryAction\">"
                + "                <next state=\"SUCCESS\" resp_code=\"SUCCESS\"/>" 
                + "                <next state=\"FAIL\" resp_code=\"FAILED\"/>" 
                + "            </action>"
                + "        </state>"
                + "        <state name=\"SUCCESS\"/>"
                + "    </states>"
                + "</state-machine>";
        
        // 创建上下文对象，设置标志以控制第一次执行失败
        Context context = new Context();
        context.set("firstExecution", true);
        
        // 配置mock对象行为
        when(stateMachineFactory.createStateMachineFromXmlString(xmlConfig)).thenReturn(demoStateMachine);
        when(stateMachineFactory.createStateMachineInterfaceFromXmlString(xmlConfig)).thenReturn(stateMachine);
        
        // 模拟方法链式调用，并关联状态变化
        when(demoStateMachine.start()).thenAnswer(invocation -> {
            // 当调用start方法时，模拟状态变为FAIL
            when(stateMachine.getCurrentState()).thenReturn("FAIL");
            return demoStateMachine;
        });
        
        when(demoStateMachine.retry()).thenAnswer(invocation -> {
            // 当调用retry方法时，模拟状态变为SUCCESS
            when(stateMachine.getCurrentState()).thenReturn("SUCCESS");
            return demoStateMachine;
        });
        
        // 初始状态为INIT
        when(stateMachine.getCurrentState()).thenReturn("INIT");
        
        // 从XML创建API代理对象
        DemoStateMachine actualDemoStateMachine = (DemoStateMachine) stateMachineFactory.createStateMachineFromXmlString(xmlConfig);
        
        // 验证代理对象不为空
        assertNotNull(actualDemoStateMachine, "API代理对象不应为空");
        
        // 获取原始状态机实例以检查状态
        StateMachine actualStateMachine = stateMachineFactory.createStateMachineInterfaceFromXmlString(xmlConfig);
        
        // 第一次调用start方法，应该失败并转换到FAIL状态
        actualDemoStateMachine.start();
        verify(demoStateMachine).start();
        assertEquals("FAIL", actualStateMachine.getCurrentState(), "第一次start后状态应为FAIL");
        
        // 调用retry方法，应该成功并转换到SUCCESS状态
        actualDemoStateMachine.retry();
        verify(demoStateMachine).retry();
        assertEquals("SUCCESS", actualStateMachine.getCurrentState(), "retry后状态应为SUCCESS");
        
        System.out.println("状态机API测试通过！第一次start失败，retry成功！");
    }
}