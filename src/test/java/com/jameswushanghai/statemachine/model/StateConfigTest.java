package com.jameswushanghai.statemachine.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * StateConfig的测试类
 */
public class StateConfigTest {

    private StateConfig stateConfig;

    @Before
    public void setUp() {
        stateConfig = new StateConfig();
    }

    @Test
    public void testSetAndGetName() {
        // 测试设置和获取名称
        String name = "INITIAL";
        stateConfig.setName(name);
        assertEquals(name, stateConfig.getName());

        // 测试设置null名称
        stateConfig.setName(null);
        assertNull(stateConfig.getName());
    }

    @Test
    public void testSetAndGetActions() {
        // 测试设置和获取动作列表
        List<ActionConfig> actions = new ArrayList<>();
        ActionConfig action1 = new ActionConfig();
        action1.setName("ACTION1");
        actions.add(action1);

        stateConfig.setActions(actions);
        assertEquals(actions, stateConfig.getActions());
        assertEquals(1, stateConfig.getActions().size());

        // 测试设置null动作列表
        stateConfig.setActions(null);
        assertNull(stateConfig.getActions());
    }

    @Test
    public void testHasActions() {
        // 测试hasActions方法
        assertFalse(stateConfig.hasActions());

        List<ActionConfig> actions = new ArrayList<>();
        stateConfig.setActions(actions);
        assertFalse(stateConfig.hasActions());

        ActionConfig action = new ActionConfig();
        actions.add(action);
        assertTrue(stateConfig.hasActions());

        // 测试null动作列表
        stateConfig.setActions(null);
        assertFalse(stateConfig.hasActions());
    }

    @Test
    public void testFindAction() {
        // 测试findAction方法
        List<ActionConfig> actions = new ArrayList<>();
        ActionConfig action1 = new ActionConfig();
        action1.setName("ACTION1");
        ActionConfig action2 = new ActionConfig();
        action2.setName("ACTION2");
        actions.add(action1);
        actions.add(action2);

        stateConfig.setActions(actions);

        assertEquals(action1, stateConfig.findAction("ACTION1"));
        assertEquals(action2, stateConfig.findAction("ACTION2"));
        assertNull(stateConfig.findAction("NON_EXISTENT"));
        assertNull(stateConfig.findAction(null));

        // 测试null动作列表
        stateConfig.setActions(null);
        assertNull(stateConfig.findAction("ACTION1"));
    }

    @Test
    public void testFindActionWithCaseSensitivity() {
        // 测试findAction方法的大小写敏感性
        List<ActionConfig> actions = new ArrayList<>();
        ActionConfig action = new ActionConfig();
        action.setName("ActionName");
        actions.add(action);
        stateConfig.setActions(actions);

        assertEquals(action, stateConfig.findAction("ActionName"));
        assertNull(stateConfig.findAction("actionname")); // 小写不匹配
        assertNull(stateConfig.findAction("ACTIONNAME")); // 大写不匹配
    }

    @Test
    public void testMultipleActionsWithSameName() {
        // 测试多个同名动作的情况（应该返回第一个匹配的）
        List<ActionConfig> actions = new ArrayList<>();
        ActionConfig action1 = new ActionConfig();
        action1.setName("DUPLICATE");
        ActionConfig action2 = new ActionConfig();
        action2.setName("DUPLICATE");
        actions.add(action1);
        actions.add(action2);

        stateConfig.setActions(actions);

        assertEquals(action1, stateConfig.findAction("DUPLICATE")); // 应该返回第一个
    }

    @Test
    public void testStateConfigEquality() {
        // 测试状态配置的相等性（这个类没有重写equals方法，所以测试引用相等）
        StateConfig config1 = new StateConfig();
        StateConfig config2 = new StateConfig();
        StateConfig config3 = config1;
        
        assertFalse(config1.equals(config2));
        assertTrue(config1.equals(config3));
    }

    @Test
    public void testAddActionsOneByOne() {
        // 测试逐个添加动作
        ActionConfig action1 = new ActionConfig();
        action1.setName("ACTION1");
        ActionConfig action2 = new ActionConfig();
        action2.setName("ACTION2");

        // 先创建动作列表
        List<ActionConfig> actions = new ArrayList<>();
        actions.add(action1);
        stateConfig.setActions(actions);
        
        // 再添加一个动作
        actions.add(action2);
        
        // 验证动作列表
        assertEquals(2, stateConfig.getActions().size());
        assertEquals(action1, stateConfig.getActions().get(0));
        assertEquals(action2, stateConfig.getActions().get(1));
    }
}