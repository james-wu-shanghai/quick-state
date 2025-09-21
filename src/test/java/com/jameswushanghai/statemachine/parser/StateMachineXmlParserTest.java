package com.jameswushanghai.statemachine.parser;

import com.jameswushanghai.statemachine.model.StateMachineConfig;
import com.jameswushanghai.statemachine.model.StateConfig;
import com.jameswushanghai.statemachine.model.ActionConfig;
import com.jameswushanghai.statemachine.model.NextState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * StateMachineXmlParser的测试类
 */
public class StateMachineXmlParserTest {

    private StateMachineXmlParser parser;

    @Before
    public void setUp() {
        parser = new StateMachineXmlParser();
    }

    @Test
    public void testParseFromStringBasic() throws Exception {
        // 测试解析基本的XML配置
        String xmlString = "<stateMachine name='testMachine'>" +
                "  <state name='INITIAL'/>" +
                "  <state name='FINAL'/>" +
                "</stateMachine>";

        // 解析XML
        StateMachineConfig config = parser.parseFromString(xmlString);

        // 验证结果
        assertNotNull(config);
        assertEquals("testMachine", config.getName());
        assertNotNull(config.getStates());
        assertEquals(2, config.getStates().size());
        assertTrue(config.containsState("INITIAL"));
        assertTrue(config.containsState("FINAL"));
    }

    @Test
    public void testParseFromStringWithActions() throws Exception {
        // 测试解析包含动作的XML配置
        String xmlString = "<stateMachine name='orderMachine'>" +
                "  <state name='CREATED'>" +
                "    <action name='PAY' ref='payAction'>" +
                "      <nextState respCode='SUCCESS' state='PAID'/>" +
                "      <nextState respCode='FAILED' state='FAILED'/>" +
                "    </action>" +
                "  </state>" +
                "  <state name='PAID'/>" +
                "  <state name='FAILED'/>" +
                "</stateMachine>";

        // 解析XML
        StateMachineConfig config = parser.parseFromString(xmlString);

        // 验证结果
        assertNotNull(config);
        assertEquals("orderMachine", config.getName());
        assertTrue(config.containsState("CREATED"));
        assertTrue(config.containsState("PAID"));
        assertTrue(config.containsState("FAILED"));

        // 验证动作配置
        StateConfig createdState = config.getState("CREATED");
        assertNotNull(createdState);
        assertTrue(createdState.hasActions());
        ActionConfig payAction = createdState.findAction("PAY");
        assertNotNull(payAction);
        assertEquals("payAction", payAction.getRef());

        // 验证下一个状态配置
        List<NextState> nextStates = payAction.getNextStates();
        assertNotNull(nextStates);
        assertEquals(2, nextStates.size());
        NextState successState = nextStates.get(0);
        assertEquals("SUCCESS", successState.getRespCode());
        assertEquals("PAID", successState.getState());
        NextState failedState = nextStates.get(1);
        assertEquals("FAILED", failedState.getRespCode());
        assertEquals("FAILED", failedState.getState());
    }

    @Test
    public void testParseFromStringMultipleStatesAndActions() throws Exception {
        // 测试解析包含多个状态和多个动作的XML配置
        String xmlString = "<stateMachine name='complexMachine'>" +
                "  <state name='S1'>" +
                "    <action name='A1' ref='action1'>" +
                "      <nextState respCode='OK' state='S2'/>" +
                "    </action>" +
                "    <action name='A2' ref='action2'>" +
                "      <nextState respCode='OK' state='S3'/>" +
                "    </action>" +
                "  </state>" +
                "  <state name='S2'>" +
                "    <action name='A3' ref='action3'>" +
                "      <nextState respCode='OK' state='S4'/>" +
                "    </action>" +
                "  </state>" +
                "  <state name='S3'/>" +
                "  <state name='S4'/>" +
                "</stateMachine>";

        // 解析XML
        StateMachineConfig config = parser.parseFromString(xmlString);

        // 验证结果
        assertNotNull(config);
        assertEquals("complexMachine", config.getName());
        assertEquals(4, config.getStates().size());

        // 验证S1状态的两个动作
        StateConfig s1 = config.getState("S1");
        assertNotNull(s1);
        assertTrue(s1.hasActions());
        assertNotNull(s1.findAction("A1"));
        assertNotNull(s1.findAction("A2"));

        // 验证S2状态的动作
        StateConfig s2 = config.getState("S2");
        assertNotNull(s2);
        assertTrue(s2.hasActions());
        assertNotNull(s2.findAction("A3"));

        // 验证S3和S4状态没有动作
        StateConfig s3 = config.getState("S3");
        assertNotNull(s3);
        assertFalse(s3.hasActions());
        
        StateConfig s4 = config.getState("S4");
        assertNotNull(s4);
        assertFalse(s4.hasActions());
    }

    @Test
    public void testParseFromInputStream() throws Exception {
        // 测试从输入流解析XML
        String xmlContent = "<stateMachine name='streamMachine'>" +
                "  <state name='S1'/>" +
                "  <state name='S2'/>" +
                "</stateMachine>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());

        // 解析XML
        StateMachineConfig config = parser.parseFromInputStream(inputStream);

        // 验证结果
        assertNotNull(config);
        assertEquals("streamMachine", config.getName());
        assertEquals(2, config.getStates().size());
        assertTrue(config.containsState("S1"));
        assertTrue(config.containsState("S2"));
    }

    @Test(expected = Exception.class)
    public void testParseFromStringInvalidXml() throws Exception {
        // 测试解析无效的XML
        String invalidXml = "invalid xml content";
        parser.parseFromString(invalidXml);
    }

    @Test(expected = Exception.class)
    public void testParseFromInputStreamInvalidXml() throws Exception {
        // 测试从输入流解析无效的XML
        String invalidXml = "invalid xml content";
        InputStream inputStream = new ByteArrayInputStream(invalidXml.getBytes());
        parser.parseFromInputStream(inputStream);
    }

    @Test
    public void testParseFromStringEmptyXml() throws Exception {
        // 测试解析空的XML（只有根元素）
        String emptyXml = "<stateMachine name='emptyMachine'></stateMachine>";
        StateMachineConfig config = parser.parseFromString(emptyXml);
        assertNotNull(config);
        assertEquals("emptyMachine", config.getName());
        assertTrue(config.getStates().isEmpty());
    }

    @Test
    public void testParseFromStringMissingName() throws Exception {
        // 测试解析缺少name属性的XML
        String xmlWithoutName = "<stateMachine>" +
                "  <state name='S1'/>" +
                "</stateMachine>";
        StateMachineConfig config = parser.parseFromString(xmlWithoutName);
        assertNotNull(config);
        assertNull(config.getName()); // name应该为null
        assertTrue(config.containsState("S1"));
    }

    @Test
    public void testParseFromStringMissingRef() throws Exception {
        // 测试解析缺少ref属性的动作
        String xmlWithoutRef = "<stateMachine name='testMachine'>" +
                "  <state name='S1'>" +
                "    <action name='A1'>" +
                "      <nextState respCode='OK' state='S2'/>" +
                "    </action>" +
                "  </state>" +
                "  <state name='S2'/>" +
                "</stateMachine>";
        StateMachineConfig config = parser.parseFromString(xmlWithoutRef);
        assertNotNull(config);
        StateConfig s1 = config.getState("S1");
        ActionConfig action = s1.findAction("A1");
        assertNotNull(action);
        assertEquals("", action.getRef()); // ref应该为空字符串
    }
}