package core.nmvc;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.di.factory.BeanFactory;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private BeanFactory beanFactory;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * beanScanner를 넘겨받아서 여기서 controller 빈을 생성하는 것을 원하는 것 같은데,
     * 디스패처 서블릿에서 모든 빈을 생성해두고 이걸 가져다 쓰는 방식이 좋지 않을까.
     */
    public void initialize() {
        Set<Class<?>> controllers = beanFactory.getAnnotatedBeans(Controller.class);
        Set<Method> methods = getRequestMappingMethods(controllers);
        for (Method method : methods) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            logger.debug("register handlerExecution : url is {}, method is {}", rm.value(), method);
            handlerExecutions.put(createHandlerKey(rm),
                    new HandlerExecution(beanFactory.getBean(method.getDeclaringClass()), method));
        }

        logger.info("Initialized AnnotationHandlerMapping!");
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

    @SuppressWarnings("unchecked")
    private Set<Method> getRequestMappingMethods(Set<Class<?>> controlleers) {
        Set<Method> requestMappingMethods = Sets.newHashSet();
        for (Class<?> clazz : controlleers) {
            requestMappingMethods
                    .addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return requestMappingMethods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        logger.debug("requestUri : {}, requestMethod : {}", requestUri, rm);
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
