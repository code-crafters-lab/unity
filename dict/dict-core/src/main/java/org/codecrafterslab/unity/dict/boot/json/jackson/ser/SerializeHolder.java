package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.codecrafterslab.unity.dict.boot.combine.Combinable;
import org.codecrafterslab.unity.dict.boot.combine.CombineStrategy;
import org.codecrafterslab.unity.dict.boot.combine.Key;
import org.codecrafterslab.unity.dict.boot.combine.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode
public class SerializeHolder implements Combinable<SerializeHolder> {
    /**
     * 全局序列化字段配置
     */
    @Nullable
    private final Map<Scope, String> scopeKeyValues;

    /**
     * 序列化范围
     */
    private final List<Key> keys;

    /**
     * 扁平化输出的 Key
     */
    @Setter
    @Nullable
    private Key flattenKey;

    public SerializeHolder() {
        this(Collections.emptyList());
    }

    public SerializeHolder(List<Key> keys) {
        this(keys, null);
    }

    public SerializeHolder(List<Key> keys, @Nullable Map<Scope, String> scopeKeyValues) {
        this.keys = keys.stream().peek(key -> {
                    if (scopeKeyValues != null) {
                        key.setValue(scopeKeyValues.get(key.getScope()));
                    }
                })
                .collect(Collectors.toList());
        this.scopeKeyValues = scopeKeyValues;
    }

    private SerializeHolder(List<SerializeScope> serializeScopes, @Nullable Map<Scope, String> scopeKeyValues,
                            boolean withDefault) {
        this.keys = SerializeScope.findScopes(serializeScopes)
                .stream().map(scope -> Key.of(scope, scopeKeyValues, withDefault))
                .collect(Collectors.toList());
        this.scopeKeyValues = scopeKeyValues;
    }

    public SerializeHolder(DictSerializeProperties properties) {
        this(properties.getScopes(), properties.getKeys(), true);
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
                .filter(keys -> !keys.isEmpty())
                .map(SerializeHolder::new)
                .reduce(SerializeHolder::combine).orElse(null);
    }

    public static SerializeHolder of(DictSerialize... conditions) {
        return of(null, conditions);
    }

    public static SerializeHolder of(BeanProperty beanProperty) {
        return of(null, getDictSerializes(beanProperty));
    }

    public static SerializeHolder of(DictSerializeProperties properties, BeanProperty beanProperty) {
        return new SerializeHolder(properties).combine(of(beanProperty));
    }

    private static DictSerialize[] getDictSerializes(BeanProperty beanProperty) {
        List<DictSerialize> anno = new ArrayList<>();
        /* 类上注解 */
        DictSerialize clazzAnnotation = beanProperty.getContextAnnotation(DictSerialize.class);
        if (clazzAnnotation != null) {
            anno.add(clazzAnnotation);
        }

        /* 获取属性上注解 */
        if (beanProperty.getMember().hasAnnotation(DictSerialize.class)) {
            DictSerialize beanPropertyAnnotation = beanProperty.getAnnotation(DictSerialize.class);
            if (beanPropertyAnnotation != null) {
                anno.add(beanPropertyAnnotation);
            }
        }
        return anno.toArray(new DictSerialize[]{});
    }

    /**
     * @return boolean
     * @deprecated Since 1.0, to be removed from 1.1, use {@link #isOutputMultiple()} instead.}
     */
    @Deprecated
    public boolean isWriteObject() {
        return keys.size() > 1;
    }

    public boolean isOutputMultiple() {
        return keys.size() > 1;
    }

    private boolean isFlattenOut() {
        return flattenKey != null && keys.contains(flattenKey);
    }

    private boolean isOutputSingle() {
        return isFlattenOut() || !isOutputMultiple();
    }

    private Object getSingleValue(DictionaryItem<?> dictItem) {
        Key key = null;
        if (isFlattenOut()) {
            key = flattenKey;
        } else if (keys.size() == 1) {
            key = keys.get(0);
        }
        if (key != null) return key.getDictionaryItemFiledValue(dictItem);
        return dictItem;
    }

    private Map<String, Object> getObjectMap(DictionaryItem<?> dictItem) {
        return keys.stream().parallel()
                .collect(HashMap::new, (map, key) -> {
                    Object value = key.getScope().getDictionaryItemFiledValue(dictItem);
                    if (Objects.isNull(value)) {
                        // todo 加一个开关，控制是否输出 null
                    }
                    map.put(key.getValueWithDefault(), value);
                }, HashMap::putAll);
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

    public Object getOutPut(DictionaryItem<?> dictionaryItem) {
        if (isOutputSingle()) return getSingleValue(dictionaryItem);
        // todo 如何动态控住输出，考虑 JsonNode
        return getObjectMap(dictionaryItem);
    }

    protected JsonNode getJsonNode(DictionaryItem<?> dictItem) {
        JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(false);
        JsonNode jsonNode = jsonNodeFactory.missingNode();
        if (isOutputMultiple()) {
            ObjectNode objectNode = jsonNodeFactory.objectNode();
            keys.forEach(key -> {
                String k = key.getValue();
                Object v = key.getDictionaryItemFiledValue(dictItem);
//                JsonNode
                objectNode.put(k, "");
            });
            return objectNode;
        } else {
            return jsonNodeFactory.textNode("");
        }

    }
}
