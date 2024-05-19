package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.Features;
import org.codecrafterslab.unity.dict.boot.json.jackson.deser.DictionaryItemDeserializer;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictionaryItemSerializer;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.Ordered;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Wu Yujie
 * @since 1.0.0
 */
public class DictJackson2ObjectMapperBuilder implements Jackson2ObjectMapperBuilderCustomizer, Ordered {
    private final DictProperties dictProperties;
    private final EnumDictProvider enumDictProvider;
    private final ConversionService conversionService;

    public DictJackson2ObjectMapperBuilder(DictProperties dictProperties,
                                           EnumDictProvider enumDictProvider,
                                           ConversionService conversionService) {
        this.dictProperties = dictProperties;
        this.enumDictProvider = enumDictProvider;
        this.conversionService = conversionService;
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        Boolean annoIntrospector = dictProperties.getFeatures().getOrDefault(Features.ANNOTATION_INTROSPECTOR, false);
        if (annoIntrospector) {
            builder.annotationIntrospector(new JacksonAnnotationIntrospector());
            builder.annotationIntrospector(primary -> AnnotationIntrospectorPair.pair(primary,
                    new DictAnnotationIntrospector()));
        }

        // 序列化注册
        builder.serializerByType(DictionaryItem.class, new DictionaryItemSerializer(dictProperties.getSerialize()));

        // 反序列化注册
        Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<>();
        Collection<Class<? extends EnumDictItem<?>>> classes = enumDictProvider.getEnumDictItem();
        for (Class<? extends EnumDictItem<?>> aClass : classes) {
            if (aClass.isEnum()) {
                deserializers.put(aClass, new DictionaryItemDeserializer<>(aClass, conversionService));
            }
        }
        builder.deserializersByType(deserializers);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
