package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.Data;
import org.codecrafterslab.unity.dict.boot.combine.Scope;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/14 19:55
 */
@Data
@ConfigurationProperties(prefix = "dict.serialize")
public class DictSerializeProperties {

    /**
     * 全局默认序列化类型
     */
    private List<SerializeScope> scopes = Collections.singletonList(SerializeScope.CODE);

    /**
     * 序列化字段的 key 值
     */
    private Map<Scope, String> keys;

}
