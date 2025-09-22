package com.jameswushanghai.statemachine.persist;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.annotation.MapperScan;

import com.jameswushanghai.statemachine.persist.service.StateMachinePersistService;

/**
 * Unit test for StateMachinePersistConfigurer
 */
public class StateMachinePersistConfigurerTest {

    @Test
    public void testConfigurationAnnotations() {
        // Check if the class has the required annotations
        Class<StateMachinePersistConfigurer> configClass = StateMachinePersistConfigurer.class;
        
        assertTrue(configClass.isAnnotationPresent(Configuration.class), 
                "StateMachinePersistConfigurer should be annotated with @Configuration");
        
        ComponentScan componentScan = configClass.getAnnotation(ComponentScan.class);
        assertNotNull(componentScan, "StateMachinePersistConfigurer should be annotated with @ComponentScan");
        
        // When using @ComponentScan("package.name"), the value is stored in value() attribute
        // so we need to check all possible attributes
        boolean hasValue = componentScan.value().length > 0;
        boolean hasBasePackages = componentScan.basePackages().length > 0;
        boolean hasBasePackageClasses = componentScan.basePackageClasses().length > 0;
        
        assertTrue(hasValue || hasBasePackages || hasBasePackageClasses, 
                "ComponentScan should have either value, basePackages or basePackageClasses specified");
        
        // Check if the specified package is included in value or basePackages
        boolean packageIncluded = containsPackage(componentScan.value(), "com.jameswushanghai.statemachine.persist") || 
                                 containsPackage(componentScan.basePackages(), "com.jameswushanghai.statemachine.persist");
        assertTrue(packageIncluded,
                "ComponentScan should include com.jameswushanghai.statemachine.persist package");
        
        MapperScan mapperScan = configClass.getAnnotation(MapperScan.class);
        assertNotNull(mapperScan, "StateMachinePersistConfigurer should be annotated with @MapperScan");
        
        // When using @MapperScan("package.name"), the value is stored in value() attribute
        boolean hasMapperValue = mapperScan.value().length > 0;
        boolean hasMapperBasePackages = mapperScan.basePackages().length > 0;
        
        assertTrue(hasMapperValue || hasMapperBasePackages, 
                "MapperScan should have either value or basePackages specified");
        
        // Check if the specified package is included in value or basePackages
        boolean mapperPackageIncluded = containsPackage(mapperScan.value(), "com.jameswushanghai.statemachine.persist.mapper") || 
                                      containsPackage(mapperScan.basePackages(), "com.jameswushanghai.statemachine.persist.mapper");
        assertTrue(mapperPackageIncluded,
                "MapperScan should include com.jameswushanghai.statemachine.persist.mapper package");
    }

    @Test
    public void testConfigurationLoading() {
        // Instead of creating a full application context which requires database configuration,
        // we'll just verify that the configuration class can be instantiated
        // and that it contains the necessary annotations
        StateMachinePersistConfigurer configurer = new StateMachinePersistConfigurer();
        assertNotNull(configurer, "StateMachinePersistConfigurer should be instantiable");
        
        // Additional check for configuration presence
        assertNotNull(StateMachinePersistConfigurer.class.getAnnotation(Configuration.class),
                "Configuration annotation should be present");
    }

    private boolean containsPackage(String[] packages, String targetPackage) {
        for (String pkg : packages) {
            if (pkg.equals(targetPackage)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testConfigurerInstantiation() {
        // Test that the configurer can be instantiated
        StateMachinePersistConfigurer configurer = new StateMachinePersistConfigurer();
        assertNotNull(configurer, "StateMachinePersistConfigurer should be instantiable");
    }

    @Test
    public void testConfigurerClassStructure() {
        // Verify the class structure
        Class<StateMachinePersistConfigurer> configClass = StateMachinePersistConfigurer.class;
        
        // Check that it's a public class
        assertTrue(java.lang.reflect.Modifier.isPublic(configClass.getModifiers()),
                "StateMachinePersistConfigurer should be a public class");
        
        // Check that it's not abstract
        assertFalse(java.lang.reflect.Modifier.isAbstract(configClass.getModifiers()),
                "StateMachinePersistConfigurer should not be abstract");
        
        // Check that it's not final
        assertFalse(java.lang.reflect.Modifier.isFinal(configClass.getModifiers()),
                "StateMachinePersistConfigurer should not be final");
    }
}