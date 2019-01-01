package core.di.factory.example;

import core.annotation.Bean;
import core.annotation.Configuration;

@Configuration
public class MyConfiguration {
    @Bean
    public ConfiguraionTestClass test(MyQnaService qnaService) {
        return new ConfiguraionTestClass(qnaService);
    }
}
