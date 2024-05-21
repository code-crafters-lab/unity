package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * @author Wu Yujie
 * @since 1.0.0
 */
@Slf4j
public class DictionaryItemSerializer extends JsonSerializer<DictionaryItem<?>> implements ContextualSerializer {

    private final SerializeHolder context;

    public DictionaryItemSerializer(DictSerializeProperties serializeProperties) {
        this.context = new SerializeHolder(serializeProperties);
    }

    public DictionaryItemSerializer(SerializeHolder context) {
        this.context = context;
    }

    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty beanProperty) throws JsonMappingException {
        if (!ObjectUtils.isEmpty(beanProperty)) {
            DictSerialize annotation1 = null, annotation2 = null;

            /* 类上注解 */
            annotation1 = beanProperty.getContextAnnotation(DictSerialize.class);

            /* 获取属性上注解 */
            if (beanProperty.getMember().hasAnnotation(DictSerialize.class)) {
                annotation2 = beanProperty.getAnnotation(DictSerialize.class);
            }

            SerializeHolder combinedHolder = context.combine(SerializeHolder.of(null, annotation1, annotation2));
            // todo 全局配置为获取到
            if (combinedHolder != context) {
                return new DictionaryItemSerializer(combinedHolder);
            } else {
                return this;
            }
        }
        return provider.findNullValueSerializer(beanProperty);
    }

    @Override
    public void serialize(DictionaryItem dictItem, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
//        JsonNode jsonNode = context.getJsonNode(dictItem);
//        gen.writeTree(jsonNode);
        Object value = context.getObject(dictItem);
        if (log.isDebugEnabled()) {
            log.debug("[OUT]\t{} => {}", dictItem, value);
        }
        gen.writeObject(value);

//        gen.
    }

}
