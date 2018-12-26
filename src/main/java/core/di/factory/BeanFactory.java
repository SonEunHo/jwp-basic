package core.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() throws Exception {
        for (Class<?> beanType: preInstanticateBeans ) {
            createBean(beanType);
        }
    }

    public void createBean(Class<?> beanType) throws Exception {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(beanType);

        if(constructor == null) {
            beans.put(beanType, beanType.newInstance());
            return;
        }

        List<Object> params = Lists.newArrayList();

        for(Class<?> paramBean : constructor.getParameterTypes()) {
            Class<?> concreteParamType = BeanFactoryUtils
                    .findConcreteClass(paramBean, preInstanticateBeans);

            if(!beans.containsKey(concreteParamType)) {
                createBean(concreteParamType);
            }

            params.add(beans.get(concreteParamType));
        }

        beans.put(beanType, constructor.newInstance(params.toArray()));
    }

    public Set<Class<?>> getAnnotatedBeans(Class<? extends Annotation> annotation) {
        return beans.keySet().stream().filter(e->
            e.isAnnotationPresent(annotation)
        ).collect(Collectors.toSet());
    }
}
