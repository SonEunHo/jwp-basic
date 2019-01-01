package core.di.factory;

import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Target;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.di.factory.example.ConfiguraionTestClass;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.MyUserController;
import core.di.factory.example.MyUserService;
import core.di.factory.example.QnaController;

public class BeanFactoryTest {
    private BeanFactory beanFactory;

    @Before
    public void setup() {
        beanFactory = new BeanFactory();
        Arrays.asList(
                new ClasspathBeanDefinitionScanner(beanFactory, "core.di.factory.example"),
                new JavaConfigurationBeanDefinitionScanner(beanFactory)
        ).forEach(s-> s.doScan());
        beanFactory.initialize();
    }

    @Test
    public void configurationDI() throws Exception {
        ConfiguraionTestClass testClass = beanFactory.getBean(ConfiguraionTestClass.class);
        assertNotNull(testClass);
    }

    @Test
    public void constructorDI() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void fieldDI() throws Exception {
        MyUserService userService = beanFactory.getBean(MyUserService.class);
        assertNotNull(userService);
        assertNotNull(userService.getUserRepository());
    }

    @Test
    public void setterDI() throws Exception {
        MyUserController userController = beanFactory.getBean(MyUserController.class);

        assertNotNull(userController);
        assertNotNull(userController.getUserService());
    }

    @After
    public void tearDown() {
        beanFactory.clear();
    }
}
