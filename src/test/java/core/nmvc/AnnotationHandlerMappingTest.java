package core.nmvc;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import core.annotation.Controller;
import core.annotation.Repository;
import core.annotation.Service;
import core.di.factory.BeanFactory;
import core.di.factory.BeanScanner;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;
    private BeanFactory beanFactory;

    @Before
    public void setup() throws Exception {
        BeanScanner beanScanner = new BeanScanner(new Class[]{ Controller.class}, "core.nmvc");
        beanFactory = new BeanFactory(beanScanner.getAllBeanTypes());
        beanFactory.initialize();
        handlerMapping = new AnnotationHandlerMapping(beanFactory);
        handlerMapping.initialize();
    }

    @Test
    public void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
}
