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
import org.springframework.core.annotation.AnnotationUtils;
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
            Class<?> rawClass = beanProperty.getType().getRawClass();
            DictSerialize annotation1 = null, annotation2 = null;
            if (DictionaryItem.class.isAssignableFrom(rawClass)) {
                /* 获取实现类上的注解 */
                if (rawClass.isAnnotationPresent(DictSerialize.class)) {
                    annotation1 = AnnotationUtils.getAnnotation(rawClass, DictSerialize.class);
                }
            } else {
                rawClass = beanProperty.getType().getContentType().getRawClass();
            }
            /* 获取属性上的注解 */
            if (beanProperty.getMember().hasAnnotation(DictSerialize.class)) {
                annotation2 = beanProperty.getAnnotation(DictSerialize.class);
                if (annotation2 == null) {
                    annotation2 = beanProperty.getContextAnnotation(DictSerialize.class);
                }

                if (annotation2 != null) {
                    annotation2 = AnnotationUtils.synthesizeAnnotation(annotation2, DictSerialize.class);
                }
            }
            SerializeHolder other = SerializeHolder.of(annotation1, annotation2);
            SerializeHolder combinedHolder = context.combine(other);
            if (combinedHolder != context) {
                return new DictionaryItemSerializer(combinedHolder);
            }
        }
        return this;
    }

    @Override
    public void serialize(DictionaryItem dictItem, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        Object value = context.getObject(dictItem);
        if (log.isDebugEnabled()) {
            log.debug("{}", value);
        }
        gen.writeObject(value);
    }

}
