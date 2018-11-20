package next.controller;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import javafx.util.Pair;
import next.controller.annotation.RequestMapping;

public class RequestMapper {
    static Map<String, Pair<Method, Controller>> reqMap;
    static List<Controller> controllerList;

    static {
        reqMap = Collections.emptyMap();
        controllerList = Lists.newArrayList(new CreateUserController(),
                                            new HomeController(),
                                            new ListUserController(),
                                            new LoginController(),
                                            new ProfileController(),
                                            new UpdateUserController());
        initRequestMap();
    }

    static void initRequestMap() {
        controllerList.stream().forEach(RequestMapper::makeRequestMap);
    }

    static  Map<String, Pair> makeRequestMap(Controller controller) {
        Method[] methods = controller.getClass().getDeclaredMethods();
        Map<String, Pair> reqMap = new HashMap<>();

        for(Method m : methods) {
            RequestMapping annotation = m.getAnnotation(RequestMapping.class);
            if(annotation != null) {
                reqMap.put(annotation.value(), new Pair<>(m, controller));
            }
        }
        return reqMap;
    }

    static Pair<Method, Controller> getMethod(String requestResource) {
        return reqMap.get(requestResource);
    }

}
