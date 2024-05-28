package org.codecrafterslab.unity.dict.boot.json.jackson.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.io.IOException;
import java.util.Collection;

@Slf4j
public class FuncEnumDictItemContainerDeserializer extends ContainerDeserializerBase<Object> implements ContextualDeserializer {
    private final JsonDeserializer<Object> contentDeserializer;
    private final ValueInstantiator valueInstantiator;
    private final ConversionService conversionService;

    public FuncEnumDictItemContainerDeserializer(CollectionDeserializer collectionDeserializer,
                                                 ConversionService conversionService) {
        super(collectionDeserializer);
        this.conversionService = conversionService;
        this.contentDeserializer = getJsonDeserializer(collectionDeserializer.getContentDeserializer());
        this.valueInstantiator = collectionDeserializer.getValueInstantiator();
    }

    public FuncEnumDictItemContainerDeserializer(ObjectArrayDeserializer objectArrayDeserializer,
                                                 ConversionService conversionService) {
        super(objectArrayDeserializer);
        this.conversionService = conversionService;
        this.contentDeserializer = getJsonDeserializer(objectArrayDeserializer.getContentDeserializer());
        this.valueInstantiator = null;
    }

    private JsonDeserializer<Object> getJsonDeserializer(JsonDeserializer<Object> contentDeserializer) {
        if (contentDeserializer != null) return contentDeserializer;
        return new EnumDictItemDeserializer(getContentType().getRawClass(), conversionService);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> valueDeserializer = ctxt.findContextualValueDeserializer(getContentType(), property);
        return this;
    }

    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        return contentDeserializer;
    }

    @Override
    public ValueInstantiator getValueInstantiator() {
        return valueInstantiator;
    }

    @SuppressWarnings("unchecked")
    protected Collection<Object> createDefaultInstance(DeserializationContext context)
            throws IOException {
        return (Collection<Object>) valueInstantiator.createUsingDefault(context);
    }


    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        if (jsonParser.isExpectedStartArrayToken() && getValueType().getRawClass().isArray()) {
            return deserializeToArray(jsonParser, context);
        } else if (jsonParser.isExpectedStartArrayToken() && Collection.class.isAssignableFrom(getValueType().getRawClass())) {
            return deserializeToCollection(jsonParser, context, createDefaultInstance(context));
        }
        return deserializeFromString(jsonParser, context, jsonParser.getText());
    }

    private Collection<Object> deserializeToCollection(JsonParser jsonParser, DeserializationContext context,
                                                       Collection<Object> result) throws IOException {
        jsonParser.setCurrentValue(result);
        JsonToken token;
        try {
            while ((token = jsonParser.nextToken()) != JsonToken.END_ARRAY) {
                Object value;
                if (token == JsonToken.VALUE_NULL) {
                    if (_skipNullValues) {
                        continue;
                    }
                    value = _nullProvider.getNullValue(context);
                } else {
                    value = contentDeserializer.deserialize(jsonParser, context);
                }
                result.add(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Object[] deserializeToArray(JsonParser jsonParser, DeserializationContext context) throws IOException {
        final ObjectBuffer buffer = context.leaseObjectBuffer();
        Object[] chunk = buffer.resetAndStart();
        int ix = 0;
        JsonToken token;
        try {
            while ((token = jsonParser.nextToken()) != JsonToken.END_ARRAY) {
                // Note: must handle null explicitly here; value deserializers won't
                Object value;

                if (token == JsonToken.VALUE_NULL) {
                    if (_skipNullValues) {
                        continue;
                    }
                    value = _nullProvider.getNullValue(context);
                } else {
                    value = contentDeserializer.deserialize(jsonParser, context);
                }
                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Object[] result = buffer.completeAndClearBuffer(chunk, ix, getContentType().getRawClass());
        context.returnObjectBuffer(buffer);
        return result;
    }

    /**
     * 字符串类型转换为实际枚举类型
     *
     * @param source     code,value,label 字符串形式
     * @param targetType 实际枚举类型
     * @return T
     */
    private Object convert(String source, TypeDescriptor targetType) {
        TypeDescriptor sourceType = TypeDescriptor.forObject(source);
        try {
            Object converted = this.conversionService.convert(source, sourceType, targetType);
            if (log.isTraceEnabled()) {
                log.trace("{} : {} => {} : {}", sourceType, source, targetType, converted);
            }
            return converted;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }

    private Object deserializeFromString(JsonParser jsonParser, DeserializationContext context, String source) {
        TypeDescriptor contentTypeDescriptor = TypeDescriptor.valueOf(getContentType().getRawClass());
        JavaType containerType = getValueType();
        if (containerType instanceof ArrayType) {
            contentTypeDescriptor = TypeDescriptor.array(contentTypeDescriptor);
        } else if (containerType instanceof CollectionType) {
            contentTypeDescriptor = TypeDescriptor.collection(containerType.getRawClass(), contentTypeDescriptor);
        } else if (containerType instanceof MapType) {
            contentTypeDescriptor = TypeDescriptor.map(containerType.getRawClass(), null, null);
        }
        return this.convert(source, contentTypeDescriptor);
    }


}
