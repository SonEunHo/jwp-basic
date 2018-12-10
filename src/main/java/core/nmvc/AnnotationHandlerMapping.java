package core.nmvc;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
        Map<HandlerKey, HandlerExecution> ret = Maps.newHashMap();

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(((String)pack).replaceAll("\\.", "/"));

        File packageFile  = new File(url.getPath());
        for(String childFileName : packageFile.list()) {
            if(childFileName.endsWith(".class")) {
                try {
                    Class controllerClasss = Class.forName(pack+"."+childFileName.substring(0, childFileName.lastIndexOf(".class")));
                    ret.putAll(makeHandlerMapFromController(controllerClasss));
                } catch (Exception e) {

                }
            }
        }

        return ret;
    }

    public Map<HandlerKey, HandlerExecution> makeHandlerMapFromController(Class controller) throws IllegalAccessException, InstantiationException{
        if(controller == null || controller.getAnnotation(Controller.class) == null) {
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
            RequestMapping methodRequestMapping = m.getAnnotation(RequestMapping.class);
            if(methodRequestMapping == null) continue;
            String requestPostfix = methodRequestMapping.value();
            RequestMethod method = methodRequestMapping.method();
            ret.put(new HandlerKey(prefix+requestPostfix, method), makeHandlerExecution(m, c));
        }

        return ret;
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
