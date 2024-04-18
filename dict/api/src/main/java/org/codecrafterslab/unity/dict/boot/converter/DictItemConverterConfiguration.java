package org.codecrafterslab.unity.dict.boot.converter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Wu Yujie
 * @since 0.2.0
 */
@Configuration
@ConditionalOnWebApplication
public class DictItemConverterConfiguration implements WebMvcConfigurer {

    /**
     * <p>默认 {@link org.springframework.core.convert.support.StringToEnumConverterFactory}</p>
     * <p>无法忽略大小写，不能多值转换</p>
     *
     * @param registry FormatterRegistry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        /* 枚举字典转换 */
        if (registry instanceof ConversionService) {
            ConversionService conversionService = (ConversionService) registry;
            registry.addConverter(new EnumDictItemConverter(conversionService));
        }
        /* 持久化字典转换 */
    }
}
