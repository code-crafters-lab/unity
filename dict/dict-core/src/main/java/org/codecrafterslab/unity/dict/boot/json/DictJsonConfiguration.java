package org.codecrafterslab.unity.dict.boot.json;

import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.json.jackson.DictJackson2ObjectMapperBuilder;
import org.codecrafterslab.unity.dict.boot.json.jackson.DictModule;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Wu Yujie
 */
@Configuration
@EnableConfigurationProperties({DictProperties.class, DictSerializeProperties.class})
public class DictJsonConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    DictModule dictModule(ApplicationContext applicationContext) {
        return new DictModule(applicationContext);
    }

    /**
     * 数据字典序列化自定义处理
     *
     * @return DictJackson2ObjectMapperBuilder
     */
    @Bean
    @ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
    DictJackson2ObjectMapperBuilder dictJackson2ObjectMapperBuilder(ApplicationContext applicationContext) {
        return new DictJackson2ObjectMapperBuilder(applicationContext);
    }

}
