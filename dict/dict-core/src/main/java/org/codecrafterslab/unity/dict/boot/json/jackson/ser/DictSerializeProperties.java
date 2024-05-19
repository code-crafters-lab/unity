package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.Data;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.persist.DataDictItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
    static class SerializeKeys implements SerializeKey {
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

        public SerializeKeys(DictSerialize dictSerialize) {
            this(dictSerialize.id(), dictSerialize.code(), "", dictSerialize.label(), dictSerialize.sort(),
                    dictSerialize.disabled(), dictSerialize.description());
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


        @Override
        public SerializeKey combine(SerializeKey other) {
            if (other == null) return this;
            return new CompositeSerializeKey(this, other);
        }

    }

    static class CompositeSerializeKey implements SerializeKey {
        private final SerializeKey _this;
        private final SerializeKey _other;

        public CompositeSerializeKey(SerializeKey _this, SerializeKey _other) {
            this._this = _this;
            this._other = _other;
        }

        @Override
        public String getIdKey() {
            return combineKey(_this.getIdKey(), _other.getIdKey());
        }

        @Override
        public String getCodeKey() {
            return combineKey(_this.getCodeKey(), _other.getCodeKey());
        }

        @Override
        public String getValueKey() {
            return combineKey(_this.getValueKey(), _other.getValueKey());
        }

        @Override
        public String getLabelKey() {
            return combineKey(_this.getLabelKey(), _other.getLabelKey());
        }

        @Override
        public String getSortKey() {
            return combineKey(_this.getSortKey(), _other.getSortKey());
        }

        @Override
        public String getDisabledKey() {
            return combineKey(_this.getDisabledKey(), _other.getDisabledKey());
        }

        @Override
        public String getDescriptionKey() {
            return combineKey(_this.getDescriptionKey(), _other.getDescriptionKey());
        }

        private String combineKey(String key, String otherKey) {
            return Stream.of(otherKey, key).filter(StringUtils::hasText).findFirst().orElse(null);
        }

        @Override
        public SerializeKey combine(SerializeKey other) {
            return null;
        }
    }
}
