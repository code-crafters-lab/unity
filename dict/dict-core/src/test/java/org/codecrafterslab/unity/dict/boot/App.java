package org.codecrafterslab.unity.dict.boot;


import org.codecrafterslab.unity.dict.boot.handler.provider.FuncEnumDictItemProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    FuncEnumDictItemProvider funcEnumDictItemProvider() {
        return () -> Collections.singletonList(ProductService.class);
    }

}