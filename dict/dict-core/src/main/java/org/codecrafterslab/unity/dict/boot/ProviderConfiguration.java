package org.codecrafterslab.unity.dict.boot;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProvider;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProviderBuilder;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProviderBuilderCustomizer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author WuYujie
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(DictProperties.class)
public class ProviderConfiguration {

    @Bean
    @ConditionalOnMissingBean
    EnumDictProviderBuilder enumDictProviderBuilder(List<EnumDictProviderBuilderCustomizer> customizers) {
        EnumDictProviderBuilder builder = new EnumDictProviderBuilder();
        for (EnumDictProviderBuilderCustomizer customizer : customizers) {
            customizer.customize(builder);
        }
        return builder;
    }

    @Bean
    @ConditionalOnClass(EnumDictProviderBuilderCustomizer.class)
    DefaultEnumDictProviderBuilderCustomizer defaultEnumDictProviderBuilderCustomizer(DictProperties dictProperties) {
        return new DefaultEnumDictProviderBuilderCustomizer(dictProperties.getEnumDictItemPackage());
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    EnumDictProvider provider(EnumDictProviderBuilder builder) {
        return builder.build();
    }

    @Slf4j
    final static class DefaultEnumDictProviderBuilderCustomizer implements EnumDictProviderBuilderCustomizer {

        private final String enumPackage;

        public DefaultEnumDictProviderBuilderCustomizer(String enumPackage) {
            this.enumPackage = enumPackage;
            if (!StringUtils.hasText(enumPackage)) {
                // todo 如果 enumPackage 为空获取应用程序所在包，自动进行扫描
                enumPackage = "";
            }
        }

        @Override
        public void customize(EnumDictProviderBuilder builder) {
            Collection<Class<? extends EnumDictItem<?>>> classes = packageScan(enumPackage);
            builder.add(classes);
        }

        private Collection<Class<? extends EnumDictItem<?>>> packageScan(String enumDictPackage) {
            if (!StringUtils.hasText(enumDictPackage)) return Collections.emptyList();

            /* 查找 package 下枚举字典实现类 */
            ClassPathScanningCandidateComponentProvider provider =
                    new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AssignableTypeFilter(EnumDictItem.class));
            Set<BeanDefinition> components = provider.findCandidateComponents(enumDictPackage);

            List<Class<? extends EnumDictItem<?>>> list = new LinkedList<>();
            for (BeanDefinition component : components) {
                try {
                    Class<?> cls = Class.forName(component.getBeanClassName());
                    @SuppressWarnings("unchecked")
                    Class<EnumDictItem<?>> baseEnumClass = (Class<EnumDictItem<?>>) cls;
                    list.add(baseEnumClass);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            return Collections.unmodifiableCollection(list);
        }
    }


}
