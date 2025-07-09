package org.codecrafterslab.unity.dict.boot;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.boot.combine.Scope;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.SerializeScope;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProvider;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProviderBuilder;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProviderBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

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
        customizers.forEach(customizer -> customizer.customize(builder));
        return builder;
    }

    @Bean
    @ConditionalOnClass(EnumDictProviderBuilderCustomizer.class)
    DefaultEnumDictProviderBuilderCustomizer defaultEnumDictProviderBuilderCustomizer(DictProperties dictProperties) {
        return new DefaultEnumDictProviderBuilderCustomizer(dictProperties);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    EnumDictProvider provider(EnumDictProviderBuilder builder) {
        return builder.build();
    }

    @Slf4j
    final static class DefaultEnumDictProviderBuilderCustomizer implements EnumDictProviderBuilderCustomizer {

        private final List<String> enumPackages;
        private final boolean globalScan;

        public DefaultEnumDictProviderBuilderCustomizer(DictProperties dictProperties) {
            this.enumPackages = new ArrayList<>(dictProperties.getEnumDictItemPackages());
            this.globalScan = dictProperties.isGlobalScan();
        }

        @Override
        public void customize(EnumDictProviderBuilder builder) {
            builder.globalScan(globalScan).scan(enumPackages).exclude(Scope.class, SerializeScope.class);
        }

    }


}
