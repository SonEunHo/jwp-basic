package next.controller;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import next.controller.annotation.RequestMapping;

public class RequestMapper {
    static Map<String, Method> reqMap;
    static List<Controller> controllerList;

    static {
        reqMap = Collections.emptyMap();
        controllerList = Lists.newArrayList(new CreateUserController(),
                                            new HomeController(),
                                            new ListUserController(),
                                            new LoginController(),
                                            new ProfileController(),
                                            new UpdateUserController());
    }

    static void initRequestMap() {
        controllerList.stream().forEach(RequestMapper::makeRequestMap);
    }

    static <T>  Map<String, Method> makeRequestMap(T controller) {
        Method[] methods = controller.getClass().getDeclaredMethods();
        Map<String, Method> reqMap = new HashMap<>();

        for(Method m : methods) {
            RequestMapping annotation = m.getAnnotation(RequestMapping.class);
            if(annotation != null) {
                reqMap.put(annotation.value(), m);
            }
        }
        return reqMap;
    }

    static Method getMethod(String requestResource) {
        return reqMap.get(requestResource);
    }

}
