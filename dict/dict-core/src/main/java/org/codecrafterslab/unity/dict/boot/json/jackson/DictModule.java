package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.json.jackson.deser.DictionaryItemDeserializer;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictionaryItemSerializer;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/09 14:05
 */
@Slf4j
public class DictModule extends SimpleModule {

    private final ApplicationContext applicationContext;

    public DictModule(ApplicationContext applicationContext) {
        super("DictModule", new Version(1, 0, 0, "beta.16", "org.codecrafterslab.unity", "dict-core"));
        this.applicationContext = applicationContext;
        this.init();
    }

    private void init() {
        this.configSerializers();
        this.configSerializerModifier();
        this.configDeserializers();
    }

    private void configSerializers() {
        // 获取配置
        DictSerializeProperties serializeProperties = applicationContext.getBean(DictSerializeProperties.class);

        // 枚举字典序列化
        addSerializer(DictionaryItem.class, new DictionaryItemSerializer<>(serializeProperties));
    }

    @SuppressWarnings("unchecked")
    private void configDeserializers() {
        EnumDictProvider enumDictProvider = applicationContext.getBean(EnumDictProvider.class);
        ConversionService conversionService = applicationContext.getBean(ConversionService.class);

        // 枚举字典反序列化注册
        enumDictProvider.getEnumDictItem().stream()
                .filter(Class::isEnum)
                .map(aClass -> (Class<EnumDictItem<?>>) aClass)
                .forEach(aClass ->
                        addDeserializer(aClass, new DictionaryItemDeserializer<>(aClass, conversionService)));
    }

    private void configSerializerModifier() {
        setSerializerModifier(new DictBeanSerializerModifier());
    }

}
