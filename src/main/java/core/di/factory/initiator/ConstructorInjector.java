package core.di.factory.initiator;

import static org.reflections.ReflectionUtils.*;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import core.annotation.Inject;
import core.di.factory.BeanFactory;
import sun.plugin.dom.exception.InvalidStateException;

public class ConstructorInjector extends AbstractBeanInjector{
    private static final Logger logger = LoggerFactory.getLogger(ConstructorInjector.class);
    public ConstructorInjector(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public Object inject(Class<?> beanType) {
        if(!canSupport(beanType)) {
            throw new InvalidStateException("cannot inject by constructor. beanType=" + beanType);
        }
        Constructor constructor = getInjectedConstructor(beanType);
        Class<?>[] paramTypes = constructor.getParameterTypes();

        Object[] paramInstances = Arrays.stream(paramTypes)
                                        .map(this::instantiateClass)
                                        .toArray();

        return BeanUtils.instantiateClass(constructor, paramInstances);
    }

    @Override
    public boolean canSupport(Class<?> beanType) {
        return getInjectedConstructor(beanType) != null;
    }

    private Constructor<?> getInjectedConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = getAllConstructors(clazz, withAnnotation(Inject.class));
        if (injectedConstructors.isEmpty()) {
            return null;
        }
        return injectedConstructors.iterator().next();
    }
}
