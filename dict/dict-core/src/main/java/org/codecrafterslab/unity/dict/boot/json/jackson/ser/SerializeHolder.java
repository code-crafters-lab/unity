package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class SerializeHolder implements SerializeCondition<SerializeHolder> {
    /**
     * 序列化字段
     */
    private List<SerializeScope> scopes;
    /**
     * 序列号键名配置
     */
    private SerializeKey keys;

    private SerializeHolder() {
    }

    private SerializeHolder(List<SerializeScope> scopes, SerializeKey keys) {
        this.scopes = SerializeScope.findInnerSerializeScope(scopes);
        this.keys = keys;
    }

    private SerializeHolder(SerializeScope[] scopes, SerializeKey keys) {
        this(Arrays.asList(scopes), keys);
    }

    public SerializeHolder(DictSerializeProperties properties) {
        this(properties.getScopes(), properties.getKeys());
    }

    public SerializeHolder(SerializeHolder base, DictSerialize... dictSerialize) {
        SerializeHolder other = SerializeHolder.of(dictSerialize);
        if (base != null) {
            other = base.combine(other);
        }
        if (other != base) {
            this.scopes = other.scopes;
            this.keys = other.keys;
        }
    }

    public static SerializeHolder of(DictSerialize... conditions) {
        return Arrays.stream(conditions)
                .filter(Objects::nonNull)
                .map(anno -> {
                    SerializeScope[] scopes = anno.scopes();
                    SerializeKey keys = new DictSerializeProperties.SerializeKeys(anno);
                    return new SerializeHolder(scopes, keys);
                }).reduce(new SerializeHolder(), SerializeHolder::combine);
    }

    public static SerializeHolder of(List<DictSerialize> dictSerializes) {
        DictSerialize[] array = dictSerializes.toArray(new DictSerialize[]{});
        return of(array);
    }

    @Override
    public SerializeHolder combine(SerializeHolder other) {
        if (other != null) {
            List<SerializeScope> scopes = this.combineScopes(this.scopes, other.scopes);
            SerializeKey keys = this.combineKeys(this.keys, other.keys);
            return new SerializeHolder(scopes, keys);
        }
        return this;
    }

    private List<SerializeScope> combineScopes(List<SerializeScope> scopes, List<SerializeScope> other) {
        return Stream.of(other, scopes).filter(Objects::nonNull).findFirst().orElse(Collections.emptyList());
    }

    private SerializeKey combineKeys(SerializeKey keys, SerializeKey other) {
        if (keys != null) return keys.combine(other);
        return other;
    }

    protected boolean isWriteObject() {
        return scopes.size() > 1;
    }

    public Object getObject(DictionaryItem<?> dictionaryItem) {
        if (isWriteObject()) return getMap(dictionaryItem);
        return getSingleValue(dictionaryItem);
    }

    private Object getSingleValue(DictionaryItem<?> dictItem) {
        Assert.isTrue(scopes.size() == 1, "不能序列化单值");
        return scopes.get(0).getValue(dictItem);
    }

    private Map<String, Object> getMap(DictionaryItem<?> dictItem) {
        HashMap<String, Object> data = new HashMap<>(scopes.size());
        scopes.forEach(scope -> {
            String key = scope.getKey(keys);
            Object value = scope.getValue(dictItem);
            data.put(key, value);
        });
        return data;
    }

}
