package org.codecrafterslab.unity.response.autoconfigure;

import org.codecrafterslab.unity.response.ResultAdvice;
import org.codecrafterslab.unity.response.ResultErrorController;
import org.codecrafterslab.unity.response.json.ResultJackson2ObjectMapperBuilder;
import org.codecrafterslab.unity.response.json.ResultSerializer;
import org.codecrafterslab.unity.response.properties.ResponseProperties;
import org.codecrafterslab.unity.response.properties.ResponseWrapperProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 配置需要在 ErrorMvcAutoConfiguration 配置前进行，否则无法覆盖默认 BasicErrorController
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2020/09/23 12:38
 * @see ResultErrorController
 * @see ResultAdvice
 */
@Configuration
@Import({ResultAdvice.class, ResultErrorController.class})
@EnableConfigurationProperties({ResponseProperties.class, ResponseWrapperProperties.class})
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class ResponseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ResultSerializer resultSerializer(ResponseProperties responseProperties) {
        return new ResultSerializer(responseProperties);
    }

    @Bean
    @ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
    ResultJackson2ObjectMapperBuilder resultJackson2ObjectMapperBuilder(ResultSerializer resultSerializer) {
        return new ResultJackson2ObjectMapperBuilder(resultSerializer);
    }

}
