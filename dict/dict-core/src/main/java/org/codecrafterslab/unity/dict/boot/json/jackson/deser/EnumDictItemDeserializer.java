package org.codecrafterslab.unity.dict.boot.json.jackson.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.io.IOException;

@Slf4j
public class EnumDictItemDeserializer extends JsonDeserializer<Object> {
    private final Class<?> clazz;
    private final ConversionService conversionService;

    public EnumDictItemDeserializer(Class<?> type, ConversionService conversionService) {
        this.clazz = type;
        this.conversionService = conversionService;
    }


    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return convert(jsonParser.getText(), TypeDescriptor.valueOf(clazz));
    }

    @Override
    public Class<?> handledType() {
        return EnumDictItem.class;
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
            return null;
        }
    }

}
