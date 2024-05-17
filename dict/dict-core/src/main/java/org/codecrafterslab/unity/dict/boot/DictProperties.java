package org.codecrafterslab.unity.dict.boot;

import lombok.Data;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.DictSerializeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "dict")
public class DictProperties {

    /**
     * 枚举包所在包路径
     */
    private String enumDictItemPackage = "org.codecrafterslab.unity.dict.boot";

    /**
     *
     */
    @NestedConfigurationProperty
    private DictSerializeProperties serialize = new DictSerializeProperties();
}
