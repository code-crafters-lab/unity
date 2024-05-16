package org.codecrafterslab.unity.dict.boot.json.jackson.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

import java.io.IOException;

@Slf4j
public class DictionaryItemDeserializer extends JsonDeserializer<DictionaryItem<?>> implements ContextualDeserializer {
    private Class<EnumDictItem<?>> type;
    private BeanProperty beanProperty;

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext,
                                                BeanProperty beanProperty) throws JsonMappingException {
        return null;
    }

    @Override
    public DictionaryItem<?> deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        log.warn("{}", p.getBigIntegerValue());
        EnumDictItem.find(null, p.getObjectId());
        if (p.hasToken(JsonToken.START_ARRAY)) {
        }
        if (p.isExpectedStartArrayToken()) {
//            deserializationContext.readPropertyValue();
//            deserializationContext.findContextualValueDeserializer()
        }
        if (p.hasToken(JsonToken.VALUE_STRING)) {
//            return EnumDictItem.findByCode(ProductServiceEnum.class, p.getText());
//            return this._fromString(p, deserializationContext, p.getText());\
//            deserializationContext.
        }
//        BeanProperty beanProperty;
//        this.fieldClass = property.getType().getRawClass();
//        this.fieldName = property.getName();
        deserializationContext.findContextualValueDeserializer(beanProperty.getType(), beanProperty);
        return null;
    }

}
