package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.SimpleType;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.SerializeHolder;

import java.util.List;

@Slf4j
public class DictBeanSerializerModifier extends BeanSerializerModifier {
    private final DictProperties dictProperties;

    public DictBeanSerializerModifier(DictProperties dictProperties) {
        this.dictProperties = dictProperties;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {

        // 获取当前 Bean 的类型、真实 Class
        for (BeanPropertyWriter beanProperty : beanProperties) {
            if (!canProcess(beanProperty)) continue;
            // 获取注解
            SerializeHolder serializeHolder = SerializeHolder.of(dictProperties.getSerialize(), beanProperty);
            if (log.isDebugEnabled()) {
                log.debug("属性名称：{} => {}", beanProperty.getName(), serializeHolder);
            }

            if (serializeHolder.isWriteObject()) {
//                serializeHolder.getKeys().forEach(key -> {
//                    String name = beanProperty.getName() + key.getValue();
//                    DictBeanPropertyWriter dictBeanPropertyWriter = new DictBeanPropertyWriter(beanProperty,
//                            PropertyName.construct(name));
//                    beanProperties.add(dictBeanPropertyWriter);
//                });

            }

            // 属性的序列化器
//            JsonSerializer<Object> serializer = beanProperty.getSerializer();
            // 属性的类型列化器
//            TypeSerializer typeSerializer = beanProperty.getTypeSerializer();
            // 是否有Null值序列化器
//            boolean hasNullSerializer = beanProperty.hasNullSerializer();
//            ScopesOutputMode outputMode = annotation.outputMode();
        }
        return super.changeProperties(config, beanDesc, beanProperties);
    }

    protected boolean canProcess(BeanPropertyWriter beanProperty) {
        JavaType type = beanProperty.getType();
        return type instanceof SimpleType && EnumDictItem.class.isAssignableFrom(type.getRawClass());
    }

}
