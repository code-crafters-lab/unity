package org.codecrafterslab.unity.dict.boot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

import java.util.List;

public class DictPropertiesPostProcessor implements BeanPostProcessor {

    private final ObjectProvider<List<DictPropertiesCustomizer>> customizers;

    public DictPropertiesPostProcessor(ObjectProvider<List<DictPropertiesCustomizer>> customizers) {
        this.customizers = customizers;
    }

    @Override
    public Object postProcessAfterInitialization(@Nullable Object bean, @Nullable String beanName) throws BeansException {
        if (bean instanceof DictProperties) {
            DictProperties properties = (DictProperties) bean;
            applyCustomizers(properties);
        }
        return bean;
    }

    private void applyCustomizers(DictProperties properties) {
        List<DictPropertiesCustomizer> list = customizers.getIfAvailable();
        if (list == null) return;
        for (DictPropertiesCustomizer customizer : list) {
            customizer.customize(properties);
        }
    }
}
