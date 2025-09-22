package com.jameswushanghai.statemachine.parser;

import com.jameswushanghai.statemachine.model.StateMachineConfig;
import com.jameswushanghai.statemachine.model.StateConfig;
import com.jameswushanghai.statemachine.model.ActionConfig;
import com.jameswushanghai.statemachine.model.NextState;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * 状态机XML解析器
 * 用于解析状态机的XML配置文件
 */
public class StateMachineXmlParser {
    
    /**
     * 从字符串解析状态机配置
     * @param xmlString XML字符串
     * @return 状态机配置对象
     * @throws Exception 解析异常
     */
    public StateMachineConfig parseFromString(String xmlString) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            
            return parseDocument(document);
        } catch (SAXException | IOException e) {
            throw new Exception("解析XML字符串失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 从输入流解析状态机配置
     * @param inputStream 输入流
     * @return 状态机配置对象
     * @throws Exception 解析异常
     */
    public StateMachineConfig parseFromInputStream(InputStream inputStream) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            
            return parseDocument(document);
        } catch (SAXException | IOException e) {
            throw new Exception("解析XML输入流失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 解析XML文档
     * @param document XML文档对象
     * @return 状态机配置对象
     */
    private StateMachineConfig parseDocument(Document document) {
        StateMachineConfig config = new StateMachineConfig();
        
        // 获取根元素
        Element root = document.getDocumentElement();
        
        // 检查name属性是否存在，如果不存在则设置为null
        String name = null;
        if (root.hasAttribute("name")) {
            name = root.getAttribute("name");
        }
        config.setName(name);
        
        // 解析API接口配置
        NodeList apiNodes = root.getElementsByTagName("api");
        if (apiNodes.getLength() > 0) {
            Element apiElement = (Element) apiNodes.item(0);
            String apiInterface = null;
            
            // 优先从元素内容中获取API接口类名
            if (apiElement.getTextContent() != null && !apiElement.getTextContent().trim().isEmpty()) {
                apiInterface = apiElement.getTextContent().trim();
            }
            // 如果元素内容为空，则从interface属性获取
            else if (apiElement.hasAttribute("interface")) {
                apiInterface = apiElement.getAttribute("interface");
            }
            
            if (apiInterface != null && !apiInterface.isEmpty()) {
                config.setApiInterface(apiInterface);
            }
        }
        
        // 解析所有状态
        NodeList stateNodes = root.getElementsByTagName("state");
        for (int i = 0; i < stateNodes.getLength(); i++) {
            Element stateElement = (Element) stateNodes.item(i);
            StateConfig stateConfig = parseState(stateElement);
            config.addState(stateConfig);
        }
        
        return config;
    }
    
    /**
     * 解析状态元素
     * @param stateElement 状态元素
     * @return 状态配置对象
     */
    private StateConfig parseState(Element stateElement) {
        StateConfig stateConfig = new StateConfig();
        String stateName = stateElement.getAttribute("name");
        stateConfig.setName(stateName);
        
        // 解析状态下的所有动作
        NodeList actionNodes = stateElement.getElementsByTagName("action");
        for (int i = 0; i < actionNodes.getLength(); i++) {
            Element actionElement = (Element) actionNodes.item(i);
            ActionConfig actionConfig = parseAction(actionElement);
            
            if (stateConfig.getActions() == null) {
                stateConfig.setActions(new java.util.ArrayList<>());
            }
            stateConfig.getActions().add(actionConfig);
        }
        
        return stateConfig;
    }
    
    /**
     * 解析动作元素
     * @param actionElement 动作元素
     * @return 动作配置对象
     */
    private ActionConfig parseAction(Element actionElement) {
        ActionConfig actionConfig = new ActionConfig();
        actionConfig.setName(actionElement.getAttribute("name"));
        actionConfig.setRef(actionElement.getAttribute("ref"));
        
        // 解析下一个状态
        NodeList nextStateNodes = actionElement.getElementsByTagName("nextState");
        for (int i = 0; i < nextStateNodes.getLength(); i++) {
            Element nextStateElement = (Element) nextStateNodes.item(i);
            NextState nextState = new NextState();
            nextState.setRespCode(nextStateElement.getAttribute("respCode"));
            nextState.setState(nextStateElement.getAttribute("state"));
            
            if (actionConfig.getNextStates() == null) {
                actionConfig.setNextStates(new java.util.ArrayList<>());
            }
            actionConfig.getNextStates().add(nextState);
        }
        
        return actionConfig;
    }
}