package org.codecrafterslab.unity.dict.boot;

import lombok.Data;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.*;

@Data
@ConfigurationProperties(prefix = "unity.dict")
public class DictProperties {

    /**
     * 字典值{@link DictionaryItem#getValue()} 持久化到数据库的方式
     */
    private PersistenceMode valuePersistenceMode = PersistenceMode.COMMA_SPLIT;

    /**
     * 枚举字典自动扫描路径
     */
    private Set<String> enumDictItemPackages;

    /**
     * enumDictItemPackage 为空时是否自动扫描应用根目录下
     */
    private boolean globalScan = true;

    /**
     * 序列化配置
     */
    @NestedConfigurationProperty
    private DictSerializeProperties serialize = new DictSerializeProperties();

    /**
     * 新功能特性配置
     */
    private Map<Features, Boolean> features = new HashMap<Features, Boolean>() {{
        put(Features.ANNOTATION_INTROSPECTOR, false);
        put(Features.FLATTEN_OUTPUT_OBJECT, false);
    }};

}
