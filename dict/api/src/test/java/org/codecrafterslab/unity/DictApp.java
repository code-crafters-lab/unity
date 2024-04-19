package org.codecrafterslab.unity;

import org.codecrafterslab.unity.dict.boot.DictController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import({DictController.class})
public class DictApp {
    public static void main(String[] args) {
        SpringApplication.run(DictApp.class, args);
    }
}
