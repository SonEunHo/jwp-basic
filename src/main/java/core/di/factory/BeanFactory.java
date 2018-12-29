package core.di.factory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import core.annotation.Controller;
import core.di.factory.initiator.AbstractBeanInjector;
import core.di.factory.initiator.ConstructorInjector;
import core.di.factory.initiator.FieldInjector;
import core.di.factory.initiator.Injector;
import core.di.factory.initiator.MethodInjector;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private List<Injector> injectors = Lists.newArrayList();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        injectors.add(new ConstructorInjector(this));
        injectors.add(new MethodInjector(this));
        injectors.add(new FieldInjector(this));

        for (Class<?> clazz : preInstanticateBeans) {
            if (beans.get(clazz) == null) {
                logger.debug("instantiated Class : {}", clazz);
                instantiateClass(clazz);
            }
        }
    }

    private Object instantiateClass(Class<?> clazz) {
        Object bean = beans.get(clazz);
        if (bean != null) {
            return bean;
        }

        bean = ((AbstractBeanInjector)injectors.get(0)).instantiateClass(clazz);
        return bean;
    }

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        for (Class<?> clazz : preInstanticateBeans) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, beans.get(clazz));
            }
        }
        return controllers;
    }

    public Class<?> getConcreteClassType(Class<?> clazz) {
        return BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
    }

    public void addBean(Class<?> clazz, Object bean) {
        beans.put(clazz, bean);
    }
}
