package core.di.factory.initiator;

import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Sets;

import core.di.factory.BeanFactory;

public abstract class AbstractBeanInjector implements Injector {
    protected final BeanFactory beanFactory;
    private static Set<Injector> injectors = Sets.newHashSet();

    public AbstractBeanInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        injectors.add(this);
    }

    public Object instantiateClass(Class<?> clazz) {
        clazz = beanFactory.getConcreteClassType(clazz);

        Object bean = beanFactory.getBean(clazz);
        if (bean != null) {
            return bean;
        }


        for (Injector injector : injectors) {
            if (injector.canSupport(clazz)) {
                bean = injector.inject(clazz);
                beanFactory.addBean(clazz, bean);
                return bean;
            }
        }

        beanFactory.addBean(clazz, BeanUtils.instantiate(clazz));
        return beanFactory.getBean(clazz);
    }
}
