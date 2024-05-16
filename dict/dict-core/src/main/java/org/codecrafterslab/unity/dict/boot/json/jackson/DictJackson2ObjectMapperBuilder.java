package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictionaryItemSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Wu Yujie
 * @since 1.0.0
 */
public class DictJackson2ObjectMapperBuilder implements Jackson2ObjectMapperBuilderCustomizer, Ordered {
    private Environment environment;
    private final DictSerializeProperties serializeProperties;

    public DictJackson2ObjectMapperBuilder(DictSerializeProperties serializeProperties) {
        this.serializeProperties = serializeProperties;
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        builder.annotationIntrospector(new JacksonAnnotationIntrospector());
        builder.annotationIntrospector(annotationIntrospector -> AnnotationIntrospectorPair.pair
                (annotationIntrospector, new DictAnnotationIntrospector()));
        builder.serializerByType(DictionaryItem.class, new DictionaryItemSerializer(serializeProperties));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
