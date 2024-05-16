package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.Data;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.persist.DataDictItem;
import org.codecrafterslab.unity.dict.boot.json.annotation.DictSerialize;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

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
    List<DictSerialize.Scope> scopes = Collections.singletonList(DictSerialize.Scope.CODE);

    /**
     * {@link DataDictItem#getId()} 字典标识序列化名称
     */
    String id = "id";

    /**
     * {@link DictionaryItem#getCode()} 字典编码实际值序列化名称
     */
    String code = "code";

    /**
     * {@link DictionaryItem#getValue()} 字典项实际值序列化名称
     */
    String value = "value";

    /**
     * {@link DictionaryItem#getLabel()} 字典项显示值序列化名称
     */
    String label = "label";

    /**
     * {@link DictionaryItem#getSort()} 字典项描述序列化名称
     */
    String sort = "sort";

    /**
     * {@link DictionaryItem#isDisabled()} 字典项描述序列化名称
     */
    String disabled = "disabled";

    /**
     * {@link DictionaryItem#getDescription()}} 字典项描述序列化名称
     */
    String description = "description";
}
