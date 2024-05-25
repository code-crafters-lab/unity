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
import java.util.ArrayList;
import java.util.List;

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
            DictSerialize[] array = getDictSerializes(beanProperty);
            SerializeHolder other = SerializeHolder.of(array);
            SerializeHolder combinedHolder = context.combine(other);
            if (combinedHolder != context) {
                return new DictionaryItemSerializer<>(combinedHolder);
            }
        }
        return this;
    }

    private static DictSerialize[] getDictSerializes(BeanProperty beanProperty) {
        List<DictSerialize> anno = new ArrayList<>();
        /* 类上注解 */
        DictSerialize clazzAnnotation = beanProperty.getContextAnnotation(DictSerialize.class);
        if (clazzAnnotation != null) {
            anno.add(clazzAnnotation);
        }

        /* 获取属性上注解 */
        if (beanProperty.getMember().hasAnnotation(DictSerialize.class)) {
            DictSerialize beanPropertyAnnotation = beanProperty.getAnnotation(DictSerialize.class);
            if (beanPropertyAnnotation != null) {
                anno.add(beanPropertyAnnotation);
            }
        }
        return anno.toArray(new DictSerialize[]{});
    }

    @Override
    public void serialize(T dictItem, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // JsonNode jsonNode = context.getJsonNode(dictItem);
        // gen.writeTree(jsonNode);
        // todo 如何动态控住输出，考虑 JsonNode
        Object value = context.getObject(dictItem);
        if (log.isDebugEnabled()) {
            log.debug("[OUT]\t{} => {}", dictItem, value);
        }
        gen.writeObject(value);
    }
}
