package org.codecrafterslab.unity.dict.boot.json.jackson.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.io.IOException;

@Slf4j
public class DictionaryItemDeserializer<T extends EnumDictItem<?>> extends JsonDeserializer<T> {
    private final Class<T> clazz;
    private final ConversionService conversionService;

    public DictionaryItemDeserializer(Class<T> type, ConversionService conversionService) {
        this.clazz = type;
        this.conversionService = conversionService;
    }

    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext,
                                                BeanProperty beanProperty) throws JsonMappingException {
        return null;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException,
            JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        T result = null;
        try {
            result = EnumDictItem.find(clazz, node.asText(), val -> {
                /* 字符串类型 => 实际泛型的类型转换 */
                TypeDescriptor sourceType = TypeDescriptor.forObject(val);
                TypeDescriptor targetType = TypeDescriptor.valueOf(EnumDictItem.getValueType(clazz));
                Object converted = null;
                try {
                    converted = this.conversionService.convert(val, sourceType, targetType);
                    if (log.isDebugEnabled()) {
                        log.debug("{} : {} => {} : {}", sourceType, val, targetType, converted);
                    }
                } catch (Exception e) {
                    converted = val;
                }
                return converted;
            });
            if (log.isDebugEnabled()) {
                log.debug("{} => {}", node, result);
            }
        } catch (Exception ignored) {
        }
        return result;
    }

}
