package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DictBeanSerializerModifier extends BeanSerializerModifier {
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {

//        JavaType javaType = beanDesc.getType();
//        Class<?> rawClass = javaType.getRawClass();
//        if (EnumDictItem.class.isAssignableFrom(rawClass)) {
//            log.debug("{} => 序列化定义", rawClass);
//        } else {
//            // 查询是否开启某个特征
//            boolean enabled = config.isEnabled(SerializationFeature.FAIL_ON_SELF_REFERENCES);
//            // 获取当前Bean 的类型、真实 Class
//            for (BeanPropertyWriter beanPropertyWriter : beanProperties) {
//                JavaType type = beanPropertyWriter.getType();
//                String name = beanPropertyWriter.getName();
//                JavaType type1 = beanPropertyWriter.getMember().getType();
//                log.debug("属性名称：{} => 属性类型：{} {}", name, type, type1);
//                // 属性的序列化器
//                JsonSerializer<Object> serializer = beanPropertyWriter.getSerializer();
//                // 属性的类型列化器
//                TypeSerializer typeSerializer = beanPropertyWriter.getTypeSerializer();
//                // 是否有Null值序列化器
//                boolean hasNullSerializer = beanPropertyWriter.hasNullSerializer();
//                // 获取注解
//                DictSerialize annotation = beanPropertyWriter.getAnnotation(DictSerialize.class);
//            }
//        }
//        return beanProperties;
        return super.changeProperties(config, beanDesc, beanProperties);
    }

    @Override
    public List<BeanPropertyWriter> orderProperties(SerializationConfig config, BeanDescription beanDesc,
                                                    List<BeanPropertyWriter> beanProperties) {
        return super.orderProperties(config, beanDesc, beanProperties);
    }
}
