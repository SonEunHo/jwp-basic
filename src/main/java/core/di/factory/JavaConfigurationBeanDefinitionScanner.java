package core.di.factory;

import java.util.Set;

import static org.reflections.ReflectionUtils.*;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.Bean;
import core.annotation.Configuration;

public class JavaConfigurationBeanDefinitionScanner implements BeanDefinitionScanner {
    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private static Logger logger = LoggerFactory.getLogger(JavaConfigurationBeanDefinitionScanner.class);

    public JavaConfigurationBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public void doScan() {
        Reflections reflections = new Reflections();
        Set<Class<?>> configurationClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        configurationClasses.forEach(configuration -> {
            try {
                final Object configurationObject = configuration.newInstance();
                getAllMethods(configuration, withAnnotation(Bean.class)).forEach(method -> {
                    Class<?> beanType = method.getReturnType();
                    BeanDefinition beanDefinition = new BeanDefinition(beanType, method, configurationObject);
                    beanDefinitionRegistry.registerBeanDefinition(beanType, beanDefinition);
                });
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("bean scan error : {}", e.getMessage());
            }
        });
    }
}
