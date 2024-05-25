package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * @author Wu Yujie
 * @since 1.0.0
 */
@Slf4j
public class DictionaryItemSerializer<T extends DictionaryItem<?>> extends JsonSerializer<T> implements ContextualSerializer {

    private final SerializeHolder context;

    public DictionaryItemSerializer(DictSerializeProperties serializeProperties) {
        this.context = new SerializeHolder(serializeProperties);
    }

    public DictionaryItemSerializer(SerializeHolder context) {
        this.context = context;
    }

    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty beanProperty) throws JsonMappingException {
        if (!ObjectUtils.isEmpty(beanProperty)) {
            SerializeHolder other = SerializeHolder.of(beanProperty);
            SerializeHolder combinedHolder = context.combine(other);
            if (combinedHolder != context) {
                return new DictionaryItemSerializer<>(combinedHolder);
            }
        }
        return this;
    }


    @Override
    public void serialize(T dictItem, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // JsonNode jsonNode = context.getJsonNode(dictItem);
        // gen.writeTree(jsonNode);
        // todo 如何动态控住输出，考虑 JsonNode
        Object value = context.getObject(dictItem);
        if (log.isDebugEnabled()) {
            log.trace("[OUT]\t{} => {}", dictItem, value);
        }
        gen.writeObject(value);
    }
}
