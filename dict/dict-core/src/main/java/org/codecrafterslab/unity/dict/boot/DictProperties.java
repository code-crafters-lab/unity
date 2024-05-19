package org.codecrafterslab.unity.dict.boot;

import lombok.Data;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "dict")
public class DictProperties {

    /**
     * 字典值{@link DictionaryItem#getValue()} 持久化到数据库的方式
     */
    private ValuePersistenceMode valuePersistenceMode = ValuePersistenceMode.COMMA_SPLIT;

    /**
     * 枚举字典自动扫描路径
     */
    private String enumDictItemPackage;

    /**
     * enumDictItemPackage 为空时是否自动扫描应用根目录下
     */
    private boolean globalScan = true;

    /**
     * 序列化配置
     */
    @NestedConfigurationProperty
    private DictSerializeProperties serialize = new DictSerializeProperties();

}
