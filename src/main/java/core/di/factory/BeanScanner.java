package core.di.factory;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Sets;

public class BeanScanner {
    private Reflections reflections;
    private Class<? extends Annotation>[] beanAnnotations;

    public BeanScanner(Class<? extends Annotation>[] beanAnnotations,
                       Object... basePackage) {
        this.beanAnnotations = beanAnnotations;
        reflections = new Reflections(basePackage);
    }

    //주입 받아야하는 클래스가 빈이 아니면 에러 발생해야할 듯.
    public Set<Class<?>> getAllBeanTypes() throws BeanCreationException {
        //Controller
        //Service
        //Repository
        Set<Class<?>> beanTypes = Sets.newHashSet();

        for(Class<? extends Annotation> annotation : beanAnnotations) {
            beanTypes.addAll(scan(annotation));
        }

        return beanTypes;
    }

    public Set<Class<?>> scan(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
