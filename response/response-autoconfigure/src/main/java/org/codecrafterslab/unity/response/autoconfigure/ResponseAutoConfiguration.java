package org.codecrafterslab.unity.response.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codecrafterslab.unity.response.ResultAdvice;
import org.codecrafterslab.unity.response.ResultErrorController;
import org.codecrafterslab.unity.response.api.Result;
import org.codecrafterslab.unity.response.json.ResultModule;
import org.codecrafterslab.unity.response.json.ResultSerializer;
import org.codecrafterslab.unity.response.properties.ResponseProperties;
import org.codecrafterslab.unity.response.properties.ResponseWrapperProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@Import({ResultErrorController.class})
@EnableConfigurationProperties({ResponseProperties.class, ResponseWrapperProperties.class})
@ConditionalOnProperty(prefix = "unity.response.wrapper", name = "enable", havingValue = "true", matchIfMissing = true)
public class ResponseAutoConfiguration implements InitializingBean {

    private final ResponseProperties properties;
    private final List<ResponsePropertiesCustomizer> responsePropertiesCustomizers;

    public ResponseAutoConfiguration(ResponseProperties properties, List<ResponsePropertiesCustomizer> responsePropertiesCustomizers) {
        this.properties = properties;
        this.responsePropertiesCustomizers = responsePropertiesCustomizers;
    }

    @Bean
    @ConditionalOnMissingBean
    ResultSerializer<? extends Result<?>> resultSerializer() {
        return new ResultSerializer<>(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    ResultModule resultModule(ResultSerializer<? extends Result<?>> resultSerializer) {
        return new ResultModule(resultSerializer);
    }

    @Bean
    @ConditionalOnMissingBean
    ResultAdvice resultAdvice(ObjectMapper objectMapper) {
        return new ResultAdvice(properties.getWrapper(), objectMapper);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(this.responsePropertiesCustomizers)) {
            for (ResponsePropertiesCustomizer customizer : this.responsePropertiesCustomizers) {
                customizer.customize(this.properties);
            }
        }
    }
}
