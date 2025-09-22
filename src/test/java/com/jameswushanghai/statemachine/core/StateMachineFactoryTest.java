package com.jameswushanghai.statemachine.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.jameswushanghai.statemachine.api.DemoStateMachine;
import com.jameswushanghai.statemachine.model.StateMachineConfig;

/**
 * StateMachineFactory的测试类
 */
public class StateMachineFactoryTest {

    @InjectMocks
    private StateMachineFactory stateMachineFactory;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStateMachineFromXmlString() throws Exception {
        // 准备XML字符串
        String xml = "<state-machine name=\"testMachine\">\n"
                + "  <state name=\"state1\">\n"
                + "    <action name=\"action1\" ref=\"testAction\">\n"
                + "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n"
                + "    </action>\n"
                + "  </state>\n"
                + "  <state name=\"state2\">\n"
                + "    <action name=\"action2\" ref=\"testAction\">\n"
                + "      <next-state response=\"SUCCESS\" state=\"state1\"/>\n"
                + "    </action>\n"
                + "  </state>\n"
                + "</state-machine>";

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 调用方法
        StateMachine stateMachine = stateMachineFactory.createStateMachineInterfaceFromXmlString(xml);

        // 验证结果
        assertNotNull(stateMachine);
        assertEquals("testMachine", stateMachine.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateStateMachineFromXmlStringEmpty() throws Exception {
        // 尝试使用空的XML字符串
        stateMachineFactory.createStateMachineFromXmlString("");
    }

    @Test(expected = Exception.class)
    public void testCreateStateMachineFromXmlStringInvalid() throws Exception {
        // 尝试使用无效的XML字符串
        stateMachineFactory.createStateMachineFromXmlString("invalid xml");
    }

    @Test
    public void testCreateStateMachineFromXml() throws Exception {
        // 准备XML配置文件路径
        String configLocation = "classpath:test-machine.xml";

        // 准备XML字符串
        String xml = "<state-machine name=\"testMachine\">\n"
                + "  <state name=\"state1\">\n"
                + "    <action name=\"action1\" ref=\"testAction\">\n"
                + "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n"
                + "    </action>\n"
                + "  </state>\n"
                + "</state-machine>";

        // 创建输入流
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

        // 模拟Resource和ResourceLoader行为
        when(resourceLoader.getResource(configLocation)).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(inputStream);

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 调用方法
        StateMachine stateMachine = stateMachineFactory.createStateMachineInterfaceFromXml(configLocation);

        // 验证结果
        assertNotNull(stateMachine);
        assertEquals("testMachine", stateMachine.getName());
    }

    @Test(expected = IOException.class)
    public void testCreateStateMachineFromXmlFileNotFound() throws Exception {
        // 准备XML配置文件路径
        String configLocation = "classpath:non-existent.xml";

        // 模拟Resource和ResourceLoader行为
        when(resourceLoader.getResource(configLocation)).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        // 调用方法，应该抛出异常
        stateMachineFactory.createStateMachineFromXml(configLocation);
    }

    @Test
    public void testAfterPropertiesSet() throws Exception {
        // 验证StateMachineFactory实现了InitializingBean接口
        assertTrue(stateMachineFactory instanceof InitializingBean);

        // 调用afterPropertiesSet方法
        stateMachineFactory.afterPropertiesSet();

        // 目前该方法只是记录日志，没有实际逻辑，所以不需要额外验证
    }

    @Test(expected = Exception.class)
    public void testXmlParsingException() throws Exception {
        // 准备无效的XML字符串
        String invalidXml = "invalid xml";

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 调用方法，应该抛出异常
        stateMachineFactory.createStateMachineInterfaceFromXmlString(invalidXml);
    }

    @Test
    public void testGetStateMachine() throws Exception {
        // 准备XML字符串
        String xml = "<state-machine name=\"testMachine\">\n"
                + "  <state name=\"state1\">\n"
                + "    <action name=\"action1\" ref=\"testAction\">\n"
                + "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n"
                + "    </action>\n"
                + "  </state>\n"
                + "</state-machine>";

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 先创建状态机
        stateMachineFactory.createStateMachineInterfaceFromXmlString(xml);

        // 然后通过名称获取状态机
        StateMachine stateMachine = stateMachineFactory.getStateMachine("testMachine");

        // 验证结果
        assertNotNull(stateMachine);
        assertEquals("testMachine", stateMachine.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStateMachineNotFound() {
        // 尝试获取不存在的状态机
        stateMachineFactory.getStateMachine("nonExistentMachine");
    }

    @Test
    public void testCreateMultipleStateMachines() throws Exception {
        // 准备第一个XML字符串
        String xml1 = "<state-machine name=\"machine1\">\n"
                + "  <state name=\"state1\">\n"
                + "    <action name=\"action1\" ref=\"testAction\">\n"
                + "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n"
                + "    </action>\n"
                + "  </state>\n"
                + "</state-machine>";

        // 准备第二个XML字符串
        String xml2 = "<state-machine name=\"machine2\">\n"
                + "  <state name=\"stateA\">\n"
                + "    <action name=\"actionA\" ref=\"testAction\">\n"
                + "      <next-state response=\"SUCCESS\" state=\"stateB\"/>\n"
                + "    </action>\n"
                + "  </state>\n"
                + "</state-machine>";

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 创建两个状态机
        StateMachine machine1 = stateMachineFactory.createStateMachineInterfaceFromXmlString(xml1);
        StateMachine machine2 = stateMachineFactory.createStateMachineInterfaceFromXmlString(xml2);

        // 验证结果
        assertNotNull(machine1);
        assertNotNull(machine2);
        assertEquals("machine1", machine1.getName());
        assertEquals("machine2", machine2.getName());
        assertNotSame(machine1, machine2); // 确保是不同的实例

        // 通过名称获取状态机并验证
        StateMachine retrievedMachine1 = stateMachineFactory.getStateMachine("machine1");
        StateMachine retrievedMachine2 = stateMachineFactory.getStateMachine("machine2");
        assertNotNull(retrievedMachine1);
        assertNotNull(retrievedMachine2);
        assertEquals(machine1, retrievedMachine1);
        assertEquals(machine2, retrievedMachine2);
    }

    /**
     * 用于测试的Action实现类
     */
    private class TestAction implements Action {
        @Override
        public String doAction(Context context) {
            return "SUCCESS";
        }
    }
    
    @Test
    public void testGetStateMachineApi() throws Exception {
        // 准备XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api>com.jameswushanghai.statemachine.api.DemoStateMachine</api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";
        
        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());
        
        // 先创建状态机
        stateMachineFactory.createStateMachineInterfaceFromXmlString(xml);
        
        // 获取API代理
        DemoStateMachine apiProxy = stateMachineFactory.getStateMachineApi("testMachine", DemoStateMachine.class);
        
        // 验证结果
        assertNotNull(apiProxy);
        assertTrue(Proxy.isProxyClass(apiProxy.getClass()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetStateMachineApiInvalidInterface() throws Exception {
        // 准备XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";
        
        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());
        
        // 先创建状态机
        stateMachineFactory.createStateMachineInterfaceFromXmlString(xml);
        
        // 尝试使用非接口类型
        stateMachineFactory.getStateMachineApi("testMachine", String.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetStateMachineApiNotFound() {
        // 尝试获取不存在的状态机的API
        stateMachineFactory.getStateMachineApi("nonExistentMachine", DemoStateMachine.class);
    }

    @Test
    public void testCreateStateMachineFromXmlStringReturnProxy() throws Exception {
        // 准备带API接口的XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api>com.jameswushanghai.statemachine.api.DemoStateMachine</api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 调用方法
        Object result = stateMachineFactory.createStateMachineFromXmlString(xml);

        // 验证结果是代理对象
        assertNotNull(result);
        assertTrue(Proxy.isProxyClass(result.getClass()));
    }

    @Test
    public void testCreateStateMachineFromXmlReturnProxy() throws Exception {
        // 准备XML配置文件路径
        String configLocation = "classpath:test-machine.xml";

        // 准备带API接口的XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api>com.jameswushanghai.statemachine.api.DemoStateMachine</api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";

        // 创建输入流
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

        // 模拟Resource和ResourceLoader行为
        when(resourceLoader.getResource(configLocation)).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(inputStream);

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 调用方法
        Object result = stateMachineFactory.createStateMachineFromXml(configLocation);

        // 验证结果是代理对象
        assertNotNull(result);
        assertTrue(Proxy.isProxyClass(result.getClass()));
    }

    @Test
    public void testCreateStateMachineApiProxyCreationFailure() throws Exception {
        // 准备带API接口的XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api>com.jameswushanghai.statemachine.api.NonExistentInterface</api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 调用方法 - 预期会创建状态机但API代理创建失败
        StateMachine stateMachine = stateMachineFactory.createStateMachineInterfaceFromXmlString(xml);

        // 验证状态机仍然被创建
        assertNotNull(stateMachine);
        assertEquals("testMachine", stateMachine.getName());
    }

    @Test
    public void testCreateStateMachineInterfaceFromXmlStringWithProxy() throws Exception {
        // 准备带API接口的XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api>com.jameswushanghai.statemachine.api.DemoStateMachine</api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 调用方法
        StateMachine stateMachine = stateMachineFactory.createStateMachineInterfaceFromXmlString(xml);

        // 验证结果是状态机实例
        assertNotNull(stateMachine);
        assertEquals("testMachine", stateMachine.getName());
    }

    @Test
    public void testCreateStateMachineInterfaceFromXmlWithProxy() throws Exception {
        // 准备XML配置文件路径
        String configLocation = "classpath:test-machine.xml";

        // 准备带API接口的XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api>com.jameswushanghai.statemachine.api.DemoStateMachine</api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";

        // 创建输入流
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

        // 模拟Resource和ResourceLoader行为
        when(resourceLoader.getResource(configLocation)).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(inputStream);

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 调用方法
        StateMachine stateMachine = stateMachineFactory.createStateMachineInterfaceFromXml(configLocation);

        // 验证结果是状态机实例
        assertNotNull(stateMachine);
        assertEquals("testMachine", stateMachine.getName());
    }

    @Test(expected = RuntimeException.class)
    public void testGetStateMachineApiCreationException() throws Exception {
        // 准备带API接口的XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api>com.jameswushanghai.statemachine.api.NonExistentInterface</api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";

        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());

        // 先创建状态机
        stateMachineFactory.createStateMachineInterfaceFromXmlString(xml);

        // 尝试获取API代理，预期会抛出RuntimeException
        stateMachineFactory.getStateMachineApi("testMachine", Object.class);
    }
    
    // 新增测试用例：测试getStateMachine方法传入null名称的情况
    @Test(expected = IllegalArgumentException.class)
    public void testGetStateMachineWithNullName() {
        stateMachineFactory.getStateMachine(null);
    }
    
    // 新增测试用例：测试getStateMachineApi方法传入null名称的情况
    @Test(expected = IllegalArgumentException.class)
    public void testGetStateMachineApiWithNullName() {
        stateMachineFactory.getStateMachineApi(null, DemoStateMachine.class);
    }
    
    // 新增测试用例：测试getStateMachineApi方法传入null接口类型的情况
    @Test(expected = IllegalArgumentException.class)
    public void testGetStateMachineApiWithNullType() throws Exception {
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api>com.jameswushanghai.statemachine.api.DemoStateMachine</api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";
        
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());
        stateMachineFactory.createStateMachineInterfaceFromXmlString(xml);
        
        stateMachineFactory.getStateMachineApi("testMachine", null);
    }
    
    // 新增测试用例：测试createStateMachine方法在API接口为空字符串时的行为
    @Test
    public void testCreateStateMachineWithEmptyApiInterface() throws Exception {
        // 准备带空API接口的XML字符串
        String xml = "<state-machine name=\"testMachine\">\n" +
                "  <api></api>\n" +
                "  <state name=\"state1\">\n" +
                "    <action name=\"action1\" ref=\"testAction\">\n" +
                "      <next-state response=\"SUCCESS\" state=\"state2\"/>\n" +
                "    </action>\n" +
                "  </state>\n" +
                "</state-machine>";
        
        // 模拟ApplicationContext行为
        when(applicationContext.getBean("testAction", Action.class)).thenReturn(new TestAction());
        
        // 调用方法
        Object result = stateMachineFactory.createStateMachineFromXmlString(xml);
        
        // 验证返回的是状态机实例而非代理对象
        assertNotNull(result);
        assertTrue(result instanceof StateMachine);
        assertFalse(Proxy.isProxyClass(result.getClass()));
        assertEquals("testMachine", ((StateMachine) result).getName());
    }
    
    // 新增测试用例：测试getStateMachineApi方法中apiInterface不匹配的情况
    @Test(expected = IllegalArgumentException.class)
    public void testGetStateMachineApiInterfaceMismatch() throws Exception {
        // 创建Mock对象模拟DefaultStateMachine
        DefaultStateMachine mockStateMachine = mock(DefaultStateMachine.class);
        StateMachineConfig mockConfig = mock(StateMachineConfig.class);
        
        // 设置mock对象的行为
        when(mockConfig.getApiInterface()).thenReturn("com.example.NonMatchingInterface");
        when(mockStateMachine.getConfig()).thenReturn(mockConfig);
        when(mockStateMachine.getName()).thenReturn("testMachine");
        
        // 将mockStateMachine添加到stateMachines映射中
        // 由于stateMachines是私有成员，我们需要通过反射来访问
        try {
            java.lang.reflect.Field field = StateMachineFactory.class.getDeclaredField("stateMachines");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, StateMachine> stateMachines = (Map<String, StateMachine>) field.get(stateMachineFactory);
            stateMachines.put("testMachine", mockStateMachine);
        } catch (Exception e) {
            fail("Failed to set stateMachines field: " + e.getMessage());
        }
        
        // 调用getStateMachineApi方法，传入不匹配的接口类型
        stateMachineFactory.getStateMachineApi("testMachine", DemoStateMachine.class);
    }
}