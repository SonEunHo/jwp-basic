package core.nmvc;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.ModelAndView;

public class AnnotationHandlerMapping {
    private Object[] basePackage;
    private Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        for(Object pack : basePackage) {
            handlerExecutions.putAll(makeHandlerMapFromPackage(pack));
        }
    }

    private Map<HandlerKey, HandlerExecution> makeHandlerMapFromPackage(Object pack) {
        try {
            Map<HandlerKey, HandlerExecution> ret = Maps.newHashMap();
            Reflections reflections = new Reflections(pack);

            for (Class controller : reflections.getTypesAnnotatedWith(Controller.class)) {
                ret.putAll(makeHandlerMapFromController(controller));
            }
            return ret;
        } catch (Exception e) {
            logger.error("fail to build request map.", e);
//            throw new ServletException("fail to build request map. errMsg = " + e.getMessage());
        }
        return Collections.emptyMap();
    }

    public Map<HandlerKey, HandlerExecution> makeHandlerMapFromController(Class controller) throws IllegalAccessException, InstantiationException{
        if(controller == null || !controller.isAnnotationPresent(Controller.class)) {
            return Collections.emptyMap();
        }

        String prefix = StringUtils.EMPTY;
        Map<HandlerKey, HandlerExecution> ret = Maps.newHashMap();

        RequestMapping rm = (RequestMapping) controller.getAnnotation(RequestMapping.class);
        if (rm != null) {
            prefix = rm.value();
        }

        Object c = controller.newInstance();

        for(Method m : controller.getMethods()) {
            if (!m.isAnnotationPresent(RequestMapping.class)) {continue;}
            HandlerKey key = makeHandlerKey(prefix, m.getAnnotation(RequestMapping.class));
            HandlerExecution execution = makeHandlerExecution(m, c);
            logger.debug("[Add to ReqeustMap] key = {}, controller={}, method={}", key, controller.getName(),
                         m.getName());
            ret.put(key, execution);
        }

        return ret;
    }

    private HandlerKey makeHandlerKey(String prefix, RequestMapping requestMapping) {
        String requestPostfix = requestMapping.value();
        RequestMethod method = requestMapping.method();
        return new HandlerKey(prefix+requestPostfix, method);
    }

    private HandlerExecution makeHandlerExecution(Method m, Object controller) {
        return (req, resp) -> (ModelAndView)m.invoke(controller, req, resp);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
