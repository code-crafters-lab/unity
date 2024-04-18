package org.codecrafterslab.unity.dict.boot.autoconfigure;

import org.codecrafterslab.unity.dict.boot.converter.DictItemConverterConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Wu Yujie
 */
@Configuration
@Import({DictItemConverterConfiguration.class})
public class DictAutoConfiguration {

}
