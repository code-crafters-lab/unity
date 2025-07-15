package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.combine.Key;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

import static org.codecrafterslab.unity.dict.boot.json.jackson.DictBeanSerializerModifier.FLATTEN_OUTPUT_KEY;

/**
 * @author Wu Yujie
 * @since 1.0.0
 */
@Slf4j
public class DictionaryItemSerializer<T extends DictionaryItem<?>> extends JsonSerializer<T> implements ContextualSerializer {

    private final SerializeHolder context;

    public DictionaryItemSerializer(DictProperties dictProperties) {
        this.context = new SerializeHolder(dictProperties.getSerialize());
    }

    public DictionaryItemSerializer(SerializeHolder context) {
        this.context = context;
    }

    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty beanProperty) throws JsonMappingException {
        if (!ObjectUtils.isEmpty(beanProperty)) {
            SerializeHolder other = SerializeHolder.of(beanProperty);
            SerializeHolder combinedHolder = context.combine(other);
            if (combinedHolder != context) {
                if (beanProperty instanceof BeanPropertyWriter) {
                    BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
                    Key flattenKey = (Key) writer.getInternalSetting(FLATTEN_OUTPUT_KEY);
                    if (!ObjectUtils.isEmpty(flattenKey)) {
                        combinedHolder.setFlattenKey(flattenKey);
                    }
                }
                return new DictionaryItemSerializer<>(combinedHolder);
            }
        }
        return this;
    }


    @Override
    public void serialize(T dictItem, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Object out = context.getOutPut(dictItem);
        if (log.isTraceEnabled()) {
            log.trace("[OUT]\t{} => {}", dictItem, out);
        }
        gen.writeObject(out);
    }
}
