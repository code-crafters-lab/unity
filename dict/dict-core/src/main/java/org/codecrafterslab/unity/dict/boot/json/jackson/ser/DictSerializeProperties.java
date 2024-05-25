package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.type.SimpleType;
import lombok.Data;
import org.codecrafterslab.unity.dict.boot.combine.Scope;
import org.codecrafterslab.unity.dict.boot.combine.ScopesOutputMode;
import org.codecrafterslab.unity.dict.boot.json.jackson.DictBeanSerializerModifier;
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
     * 全局默认序列化范围
     * @see SerializeScope
     */
    private List<SerializeScope> scopes = Collections.singletonList(SerializeScope.CODE);

    /**
     * {@link BeanPropertyWriter#getType()}类型为{@link SimpleType}的属性序列化范围输出模式
     *
     * @see DictBeanSerializerModifier#canProcess(BeanPropertyWriter)
     */
    private ScopesOutputMode outputMode = ScopesOutputMode.OBJECT;

    /**
     * 序列化字段的 key 值
     * @see Scope
     */
    private Map<Scope, String> keys;

}
