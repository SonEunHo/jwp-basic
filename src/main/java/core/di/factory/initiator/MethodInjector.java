package core.di.factory.initiator;

import static org.reflections.ReflectionUtils.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import core.annotation.Inject;
import core.di.factory.BeanFactory;

public class MethodInjector extends AbstractBeanInjector {
    private static final Logger logger = LoggerFactory.getLogger(MethodInjector.class);

    public MethodInjector(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public Object inject(Class<?> beanType) {
        Object bean = BeanUtils.instantiate(beanType);
        Set<Method> methods = getInjectedMethods(beanType);

        methods.stream().forEach(method -> {
            Object[] params = Arrays.stream(method.getParameterTypes())
                                    .map(this::instantiateClass)
                                    .toArray();

            try {
                method.invoke(bean, params);
            } catch (IllegalAccessException | InvocationTargetException e ) {
                logger.error("failed to inject by method . method={}, error={}", method, e.getMessage());
            }
        });

        return bean;
    }

    @Override
    public boolean canSupport(Class<?> beanType) {
        return !getInjectedMethods(beanType).isEmpty();
    }

    private Set<Method> getInjectedMethods(Class<?> beanType) {
        return getAllMethods(beanType, withAnnotation(Inject.class));
    }
}
