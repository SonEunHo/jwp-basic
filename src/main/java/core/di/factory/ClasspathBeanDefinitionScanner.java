package core.di.factory;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Sets;

import core.annotation.Controller;
import core.annotation.Repository;
import core.annotation.Service;

public class ClasspathBeanDefinitionScanner implements BeanDefinitionScanner {
    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final Object[] basePackages;
    public ClasspathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry, Object... basePackages) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.basePackages = basePackages;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doScan() {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> beanClasses = getTypesAnnotatedWith(reflections, Controller.class, Service.class,
                Repository.class);
        for (Class<?> clazz : beanClasses) {
            beanDefinitionRegistry.registerBeanDefinition(clazz, new BeanDefinition(clazz));
        }
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> preInstantiatedBeans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            preInstantiatedBeans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return preInstantiatedBeans;
    }
}
