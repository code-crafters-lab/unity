package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.json.jackson.deser.DictionaryItemDeserializer;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictionaryItemSerializer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.Ordered;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Wu Yujie
 * @since 1.0.0
 */
public class DictJackson2ObjectMapperBuilder implements Jackson2ObjectMapperBuilderCustomizer, Ordered {
    private final DictSerializeProperties serializeProperties;
    private final DictProperties dictProperties;

    private final ConversionService conversionService;

    public DictJackson2ObjectMapperBuilder(DictProperties dictProperties,
                                           ConversionService conversionService) {
        this.dictProperties = dictProperties;
        this.serializeProperties = dictProperties.getSerialize();
        this.conversionService = conversionService;
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        builder.annotationIntrospector(new JacksonAnnotationIntrospector());
        builder.annotationIntrospector(annotationIntrospector -> AnnotationIntrospectorPair.pair
                (annotationIntrospector, new DictAnnotationIntrospector()));

        builder.serializerByType(DictionaryItem.class, new DictionaryItemSerializer(serializeProperties));

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(EnumDictItem.class));
        Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<>();
        String enumDictPackage = dictProperties.getEnumDictItemPackage();
        Set<BeanDefinition> components = provider.findCandidateComponents(enumDictPackage);

        for (BeanDefinition component : components) {
            try {
                Class<?> cls = Class.forName(component.getBeanClassName());
                if (cls.isEnum()) {
                    @SuppressWarnings({"unchecked"})
                    Class<EnumDictItem<?>> baseEnumClass = (Class<EnumDictItem<?>>) cls;
                    deserializers.put(cls, new DictionaryItemDeserializer<>(baseEnumClass, conversionService));
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        builder.deserializersByType(deserializers);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
