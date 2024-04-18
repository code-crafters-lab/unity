package org.codecrafterslab.unity.dict.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DictApp {
    public static void main(String[] args) {
        SpringApplication.run(DictApp.class, args);
    }
}
