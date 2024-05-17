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
import org.codecrafterslab.unity.dict.boot.provider.EnumDictItemProvider;
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
    private final DictSerializeProperties serializeProperties;

    private final ConversionService conversionService;
    private final EnumDictItemProvider.Builder provider;

    public DictJackson2ObjectMapperBuilder(DictProperties dictProperties,
                                           EnumDictItemProvider.Builder provider,
                                           ConversionService conversionService) {
        this.provider = provider;
        this.serializeProperties = dictProperties.getSerialize();
        this.conversionService = conversionService;
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        builder.annotationIntrospector(new JacksonAnnotationIntrospector());
        builder.annotationIntrospector(annotationIntrospector -> AnnotationIntrospectorPair.pair
                (annotationIntrospector, new DictAnnotationIntrospector()));
        // 序列化注册
        builder.serializerByType(DictionaryItem.class, new DictionaryItemSerializer(serializeProperties));

        // 反序列化注册
        Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<>();
        Collection<Class<? extends EnumDictItem<?>>> classes = provider.get();
        for (Class<? extends EnumDictItem<?>> aClass : classes) {
            deserializers.put(aClass, new DictionaryItemDeserializer<>(aClass, conversionService));
        }
        builder.deserializersByType(deserializers);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
