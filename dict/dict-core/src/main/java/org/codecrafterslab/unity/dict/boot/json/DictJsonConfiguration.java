package org.codecrafterslab.unity.dict.boot.json;

import org.codecrafterslab.unity.dict.boot.json.jackson.DictJackson2ObjectMapperBuilder;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Wu Yujie
 */
@Configuration
@EnableConfigurationProperties(DictSerializeProperties.class)
public class DictJsonConfiguration {

    /**
     * 数据字典序列化自定义处理
     *
     * @return DictJackson2ObjectMapperBuilder
     */
    @Bean
    @ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
    DictJackson2ObjectMapperBuilder dictJackson2ObjectMapperBuilder() {
        return new DictJackson2ObjectMapperBuilder();
    }

}
