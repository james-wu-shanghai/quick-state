package com.jameswushanghai.statemachine;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * 不依赖任何框架的简单TestNG测试类
 */
public class VerySimpleTest {
    
    public VerySimpleTest() {
        // 默认构造函数
        System.out.println("VerySimpleTest实例化成功");
    }
    
    @Test
    public void testVerySimple() {
        System.out.println("测试方法执行");
        assertTrue(true, "简单测试通过");
    }
}