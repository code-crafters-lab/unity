package org.codecrafterslab.unity.dict.boot.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Wu Yujie
 * @since 0.2.0
 */
@Configuration
public class DictItemConverterConfiguration implements WebMvcConfigurer {

    /**
     * <p>默认 {@link org.springframework.core.convert.support.StringToEnumConverterFactory}</p>
     * <p>无法忽略大小写，不能多值转换</p>
     *
     * @param registry FormatterRegistry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        if (registry instanceof ConversionService) {
            enumDictItemConverterConversionService(registry, (ConversionService) registry);
        }
    }

    /**
     * 枚举字典转换
     *
     * @param registry          FormatterRegistry
     * @param conversionService ConversionService
     */
    private void enumDictItemConverterConversionService(FormatterRegistry registry,
                                                        ConversionService conversionService) {
        registry.addConverter(new EnumDictItemConverter(conversionService));
    }

}
