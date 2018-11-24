package next.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import next.controller.annotation.RequestMapping;

public class RequestMapper {
    static Map<String, RequestMapNode> reqMap;
    static List<Controller> controllerList;

    static {
        reqMap = new HashMap<>();
        controllerList = Lists.newArrayList(new CreateUserController(),
                                            new HomeController(),
                                            new ListUserController(),
                                            new LoginController(),
                                            new ProfileController(),
                                            new UpdateUserController());
        initRequestMap();
    }

    static void initRequestMap() {
        for(Controller c : controllerList) {
            reqMap.putAll(makeRequestMap(c));
        }
//        controllerList.stream().forEach(c->reqMap.putAll(makeRequestMap(c)));
    }

    static  Map<String, RequestMapNode> makeRequestMap(Controller controller) {
        Method[] methods = controller.getClass().getDeclaredMethods();
        Map<String, RequestMapNode> reqMap = new HashMap<>();

        for(Method m : methods) {
            RequestMapping annotation = m.getAnnotation(RequestMapping.class);
            if(annotation != null) {
                reqMap.put(annotation.value(), new RequestMapNode(m, controller));
            }
        }
        return reqMap;
    }

    static RequestMapNode getMethod(String requestResource) {
        return reqMap.get(requestResource);
    }

    static class RequestMapNode {
        final Method method;
        final Controller controller;

        public RequestMapNode(Method method, Controller controller) {
            this.method = method;
            this.controller = controller;
        }
    }
}
