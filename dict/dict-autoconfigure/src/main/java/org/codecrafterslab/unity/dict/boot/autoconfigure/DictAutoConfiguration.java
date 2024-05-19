package org.codecrafterslab.unity.dict.boot.autoconfigure;

import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.ProviderConfiguration;
import org.codecrafterslab.unity.dict.boot.converter.DictItemConverterConfiguration;
import org.codecrafterslab.unity.dict.boot.handler.TypeHandlerConfiguration;
import org.codecrafterslab.unity.dict.boot.json.DictJsonConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Wu Yujie
 */
@Configuration
@EnableConfigurationProperties(DictProperties.class)
@Import({ProviderConfiguration.class, DictItemConverterConfiguration.class,
        TypeHandlerConfiguration.class, DictJsonConfiguration.class})
public class DictAutoConfiguration {

}
