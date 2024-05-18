package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.Data;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.persist.DataDictItem;
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
    List<SerializeScope> scopes = Collections.singletonList(SerializeScope.CODE);

    /**
     * 序列化为对象的 key 值
     */
    SerializeKeys keys = new SerializeKeys();

    @Data
    public static class SerializeKeys implements SerializeKey {
        /**
         * {@link DataDictItem#getId()} 字典标识序列化名称
         */
        String idKey;

        /**
         * {@link DictionaryItem#getCode()} 字典编码实际值序列化名称
         */
        String codeKey;

        /**
         * {@link DictionaryItem#getValue()} 字典项实际值序列化名称
         */
        String valueKey;

        /**
         * {@link DictionaryItem#getLabel()} 字典项显示值序列化名称
         */
        String labelKey;

        /**
         * {@link DictionaryItem#getSort()} 字典项描述序列化名称
         */
        String sortKey;

        /**
         * {@link DictionaryItem#isDisabled()} 字典项描述序列化名称
         */
        String disabledKey;

        /**
         * {@link DictionaryItem#getDescription()}} 字典项描述序列化名称
         */
        String descriptionKey;

        public SerializeKeys() {
            this("id", "code", "value", "label", "sort", "disabled", "description");
        }

        public SerializeKeys(String idKey, String codeKey, String valueKey, String labelKey, String sortKey,
                             String disabledKey, String descriptionKey) {
            this.idKey = idKey;
            this.codeKey = codeKey;
            this.valueKey = valueKey;
            this.labelKey = labelKey;
            this.sortKey = sortKey;
            this.disabledKey = disabledKey;
            this.descriptionKey = descriptionKey;
        }
    }
}
