package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.Features;
import org.codecrafterslab.unity.dict.boot.json.jackson.deser.FuncEnumDictItemContainerDeserializer;
import org.springframework.core.convert.ConversionService;

@Slf4j
public class DictBeanDeserializerModifier extends BeanDeserializerModifier {
    private final DictProperties dictProperties;
    private final ConversionService conversionService;


    public DictBeanDeserializerModifier(DictProperties dictProperties, ConversionService conversionService) {
        this.dictProperties = dictProperties;
        this.conversionService = conversionService;
    }

    @Override
    public JsonDeserializer<?> modifyCollectionDeserializer(DeserializationConfig config, CollectionType type,
                                                            BeanDescription beanDesc,
                                                            JsonDeserializer<?> deserializer) {
        if (canFunctionPointDeserialization(beanDesc, type.getContentType())) {
            CollectionDeserializer collectionDeserializer = (CollectionDeserializer) deserializer;
            return new FuncEnumDictItemContainerDeserializer(collectionDeserializer, conversionService);
        }
        return super.modifyCollectionDeserializer(config, type, beanDesc, deserializer);
    }

    @Override
    public JsonDeserializer<?> modifyArrayDeserializer(DeserializationConfig config, ArrayType valueType,
                                                       BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        if (canFunctionPointDeserialization(beanDesc, valueType.getContentType())) {
            ObjectArrayDeserializer objectArrayDeserializer = (ObjectArrayDeserializer) deserializer;
            return new FuncEnumDictItemContainerDeserializer(objectArrayDeserializer, conversionService);
        }
        return super.modifyArrayDeserializer(config, valueType, beanDesc, deserializer);
    }

    private boolean canFunctionPointDeserialization(BeanDescription beanDesc, JavaType contentType) {
        return dictProperties.getFeatures().getOrDefault(Features.FUNCTION_POINT_DESERIALIZATION, false)
                && FuncEnumDictItem.class.isAssignableFrom(contentType.getRawClass());
    }
}
