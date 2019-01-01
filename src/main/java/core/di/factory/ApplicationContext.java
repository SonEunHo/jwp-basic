package core.di.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ApplicationContext {
    private BeanFactory beanFactory;
    private List<BeanDefinitionScanner> scanners;

    public ApplicationContext(Object... basePackages) {
        beanFactory = new BeanFactory();
        scanners = Arrays.asList(
                new ClasspathBeanDefinitionScanner(beanFactory, basePackages),
                new JavaConfigurationBeanDefinitionScanner(beanFactory)
        );
        scanners.forEach(s-> s.doScan());
        beanFactory.initialize();
    }

    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }
}
