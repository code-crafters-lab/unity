package org.codecrafterslab.unity.dict.boot.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.json.jackson.DictJackson2ObjectMapperBuilder;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictItemProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.*;

/**
 * @author Wu Yujie
 */
@Configuration
@EnableConfigurationProperties({DictSerializeProperties.class, DictProperties.class})
public class DictJsonConfiguration {

    /**
     * 数据字典序列化自定义处理
     *
     * @return DictJackson2ObjectMapperBuilder
     */
    @Bean
    @ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
    DictJackson2ObjectMapperBuilder dictJackson2ObjectMapperBuilder(DictProperties dictProperties,
                                                                    EnumDictItemProvider.Builder builder,
                                                                    ConversionService conversionService) {
        return new DictJackson2ObjectMapperBuilder(dictProperties, builder, conversionService);
    }


    @Configuration
    @Slf4j
    static class EnumDictItemProviderConfiguration {

        @Bean
        @ConditionalOnMissingBean
        EnumDictItemProvider.Builder enumDictItemProviderBuilder(ObjectProvider<EnumDictItemProvider> providers) {
            return new EnumDictItemProvider.Builder(providers);
        }

        @Bean
        EnumDictItemProvider enumDictItemProvider(DictProperties dictProperties) {
            Set<Class<? extends EnumDictItem<?>>> classes = enumDictItems(dictProperties.getEnumDictItemPackage());
            // 类型枚举字典自动扫描
            return () -> classes;
        }

        private Set<Class<? extends EnumDictItem<?>>> enumDictItems(String enumDictPackage) {
            log.warn("{}", enumDictPackage);
            ClassPathScanningCandidateComponentProvider provider =
                    new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AssignableTypeFilter(EnumDictItem.class));
            Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<>();
            Set<BeanDefinition> components = provider.findCandidateComponents(enumDictPackage);

            List<Class<? extends EnumDictItem<?>>> list = new LinkedList<>();
            for (BeanDefinition component : components) {
                try {
                    Class<?> cls = Class.forName(component.getBeanClassName());
                    if (cls.isEnum()) {
                        @SuppressWarnings({"unchecked"})
                        Class<EnumDictItem<?>> baseEnumClass = (Class<EnumDictItem<?>>) cls;
                        list.add(baseEnumClass);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            HashSet<Class<? extends EnumDictItem<?>>> result = new HashSet<>(list);
            list.clear();
            list = null;
            return result;
        }
    }
}
