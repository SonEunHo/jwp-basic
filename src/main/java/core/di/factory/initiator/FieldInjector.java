package core.di.factory.initiator;

import static org.reflections.ReflectionUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import core.annotation.Inject;
import core.di.factory.BeanFactory;

public class FieldInjector extends AbstractBeanInjector {
    private static final Logger logger = LoggerFactory.getLogger(FieldInjector.class);

    public FieldInjector(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public Object inject(Class<?> beanType) {
        Object bean = BeanUtils.instantiate(beanType);

        Set<Field> fields = getInjectedFields(beanType);
        fields.stream().forEach(field -> {
            try {
                field.set(bean, instantiateClass(field.getType()));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error("fail to inject field. field={}, error={}",field, e.getMessage());
            }
        });

        return bean;
    }

    @Override
    public boolean canSupport(Class<?> beanType) {
        return !getInjectedFields(beanType).isEmpty();
    }

    private Set<Field> getInjectedFields(Class<?> clazz) {
        return getAllFields(clazz, withAnnotation(Inject.class));
    }
}
