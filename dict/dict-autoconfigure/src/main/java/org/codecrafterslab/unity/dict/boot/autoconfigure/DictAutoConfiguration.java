package org.codecrafterslab.unity.dict.boot.autoconfigure;

import org.codecrafterslab.unity.dict.boot.converter.DictItemConverterConfiguration;
import org.codecrafterslab.unity.dict.boot.handler.TypeHandlerConfiguration;
import org.codecrafterslab.unity.dict.boot.json.DictJsonConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Wu Yujie
 */
@Configuration
@Import({DictItemConverterConfiguration.class, TypeHandlerConfiguration.class, DictJsonConfiguration.class})
public class DictAutoConfiguration {

}
