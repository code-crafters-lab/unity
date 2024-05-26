package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.NameTransformer;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.combine.ScopesMode;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.SerializeHolder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DictBeanSerializerModifier extends BeanSerializerModifier {
    public static final String FLATTEN_OUTPUT_KEY = "FLATTEN_OUTPUT_KEY";
    private final DictProperties dictProperties;

    public DictBeanSerializerModifier(DictProperties dictProperties) {
        this.dictProperties = dictProperties;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        List<BeanPropertyWriter> newBeanProperties = new ArrayList<>();
        for (BeanPropertyWriter beanProperty : beanProperties) {
            if (!canProcess(beanProperty)) {
                newBeanProperties.add(beanProperty);
                continue;
            }

            // 获取注解
            SerializeHolder serializeHolder = SerializeHolder.of(dictProperties.getSerialize(), beanProperty);
            if (log.isDebugEnabled()) {
                log.debug("属性名称：{} => {}", beanProperty.getName(), serializeHolder);
            }

            if (!serializeHolder.isOutputMultiple()) {
                newBeanProperties.add(beanProperty);
                continue;
            }

            // todo 从上下文中获取 outputMode，这里先写死
            ScopesMode outputMode = ScopesMode.FLAT_WITH_OBJECT;

            switch (outputMode) {
                case NO_OP:
                    newBeanProperties.add(beanProperty);
                    continue;
                case FLAT:
                case FLAT_WITH_OBJECT:
                    newBeanProperties.add(beanProperty);
                default:
                    // 输出多属性
                    serializeHolder.getKeys().forEach(key -> {
                        BeanPropertyWriter dictBeanPropertyWriter =
                                beanProperty.rename(NameTransformer.simpleTransformer("",
                                        StringUtils.capitalize(key.getValueWithDefault())));
                        // 将需要扁平化的 Key 暴露出去，方便序列化进行特殊处理
                        dictBeanPropertyWriter.setInternalSetting(FLATTEN_OUTPUT_KEY, key);
                        newBeanProperties.add(dictBeanPropertyWriter);
                    });

            }

        }
        return newBeanProperties;
    }

    protected boolean canProcess(BeanPropertyWriter beanProperty) {
        JavaType type = beanProperty.getType();
        return type instanceof SimpleType && EnumDictItem.class.isAssignableFrom(type.getRawClass());
    }

}
