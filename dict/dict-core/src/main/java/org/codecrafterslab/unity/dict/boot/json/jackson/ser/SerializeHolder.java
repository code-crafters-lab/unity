package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.codecrafterslab.unity.dict.boot.combine.Combinable;
import org.codecrafterslab.unity.dict.boot.combine.CombineStrategy;
import org.codecrafterslab.unity.dict.boot.combine.Key;
import org.codecrafterslab.unity.dict.boot.combine.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@EqualsAndHashCode
public class SerializeHolder implements Combinable<SerializeHolder> {
    /**
     * 全局序列化字段配置
     */
    private final Map<Scope, String> scopeKeyValues;

    /**
     * 序列化范围
     */
    private final List<Key> keys;

    public SerializeHolder() {
        this(Collections.emptyList());
    }

    public SerializeHolder(List<Key> keys) {
        this(keys, null);
    }

    public SerializeHolder(List<Key> keys, Map<Scope, String> scopeKeyValues) {
        this.keys = keys.stream().peek(key -> {
                    if (scopeKeyValues != null) {
                        key.setValue(scopeKeyValues.get(key.getScope()));
                    }
                })
                .collect(Collectors.toList());
        this.scopeKeyValues = scopeKeyValues;
    }

    private SerializeHolder(List<SerializeScope> serializeScopes, Map<Scope, String> scopeKeyValues,
                            boolean withDefault) {
        this.keys = SerializeScope.findScopes(serializeScopes)
                .stream().map(scope -> Key.of(scope, scopeKeyValues, withDefault))
                .collect(Collectors.toList());
        this.scopeKeyValues = scopeKeyValues;
    }

    public SerializeHolder(DictSerializeProperties properties) {
        this(properties.getScopes(), properties.getKeys(), true);
    }


    @Deprecated
    public SerializeHolder(SerializeScope[] serializeScopes) {
        this(Arrays.asList(serializeScopes), null, false);
    }

    /**
     * 创建序列化上下数据持有对象
     *
     * @param scopeKeyValues 全局配置
     * @param conditions     注解配置
     * @return SerializeHolder
     */
    public static SerializeHolder of(Map<Scope, String> scopeKeyValues, DictSerialize... conditions) {
        return Arrays.stream(conditions)
                .filter(Objects::nonNull)
                .map(anno -> AnnotationUtils.synthesizeAnnotation(anno, DictSerialize.class))
                .map(anno -> SerializeScope.findScopes(anno.scopes()).stream()
                        .map(scope -> Key.of(scope, anno, scopeKeyValues))
                        .collect(Collectors.toList()))
                .map(SerializeHolder::new)
                .reduce(new SerializeHolder(), SerializeHolder::combine);
    }

    public static SerializeHolder of(DictSerialize... conditions) {
        return Arrays.stream(conditions)
                .filter(Objects::nonNull)
                .map(anno -> AnnotationUtils.synthesizeAnnotation(anno, DictSerialize.class))
                .map(anno -> SerializeScope.findScopes(anno.scopes()).stream()
                        .map(scope -> Key.of(scope, anno, null))
                        .collect(Collectors.toList()))
                .map(SerializeHolder::new)
                .reduce(new SerializeHolder(), SerializeHolder::combine);
    }


    public static SerializeHolder of(List<DictSerialize> dictSerializes) {
        return of(null, dictSerializes.toArray(new DictSerialize[]{}));
    }

    protected boolean isWriteObject() {
        return keys.size() > 1;
    }

    private Object getSingleValue(DictionaryItem<?> dictItem) {
        Assert.isTrue(keys.size() == 1, "不能序列化单值");
        return keys.get(0).getDictionaryItemFiledValue(dictItem);
    }

    private Map<String, Object> getMap(DictionaryItem<?> dictItem) {
        HashMap<String, Object> data = new HashMap<>(keys.size());
        keys.forEach(key -> data.put(key.getValue(), key.getScope().getDictionaryItemFiledValue(dictItem)));
        return data;
    }

    @Override
    public SerializeHolder combine(SerializeHolder other, CombineStrategy strategy) {
        if (!this.equals(other) && other != null) {
            List<Key> keys = Key.combine(this.keys, other.keys, strategy);
            if (!this.keys.equals(keys)) {
                // todo scopeKeyValues 同样按照 strategy 策略进行合并
                return new SerializeHolder(keys,
                        other.scopeKeyValues != null ? other.getScopeKeyValues() : this.getScopeKeyValues());
            }
        }
        return this;
    }

    public Object getObject(DictionaryItem<?> dictionaryItem) {
        if (isWriteObject()) return getMap(dictionaryItem);
        return getSingleValue(dictionaryItem);
    }

    public JsonNode getJsonNode(DictionaryItem<?> dictItem) {
        JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(false);
        if (isWriteObject()) {
            ObjectNode objectNode = jsonNodeFactory.objectNode();
            keys.forEach(key -> {
//                String value1 = key.getValue();
                Object value = key.getDictionaryItemFiledValue(dictItem);
//                objectNode.put(key.getValue(), value);
            });
            return objectNode;
        } else {
            return jsonNodeFactory.textNode("");
        }

    }
}
