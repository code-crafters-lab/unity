package org.codecrafterslab.unity.dict.boot;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(DictProperties.class)
public class DictPropertiesConfiguration {

    @Bean
    @ConditionalOnMissingBean
    DictPropertiesPostProcessor dictPropertiesPostProcessor(ObjectProvider<List<DictPropertiesCustomizer>> customizers) {
        return new DictPropertiesPostProcessor(customizers);
    }
}
