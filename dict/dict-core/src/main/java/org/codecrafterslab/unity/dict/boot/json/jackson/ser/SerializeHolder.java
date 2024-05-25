package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.codecrafterslab.unity.dict.boot.combine.Combinable;
import org.codecrafterslab.unity.dict.boot.combine.CombineStrategy;
import org.codecrafterslab.unity.dict.boot.combine.Key;
import org.codecrafterslab.unity.dict.boot.combine.Scope;
import org.springframework.core.annotation.AnnotationUtils;

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

    public boolean isWriteObject() {
        return keys.size() > 1;
    }

    private Object getSingleValue(DictionaryItem<?> dictItem) {
        if (keys.size() == 1) {
            return keys.get(0).getDictionaryItemFiledValue(dictItem);
        }
        return dictItem;
    }

    private Map<String, Object> getMap(DictionaryItem<?> dictItem) {
        return keys.stream().parallel()
                .collect(HashMap::new, (map, key) -> {
                    Object value = key.getScope().getDictionaryItemFiledValue(dictItem);
//                    if (Objects.isNull(value)) {
//                        // todo 加一个开关，控制是否输出 null
//                    }
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

    public Object getObject(DictionaryItem<?> dictionaryItem) {
        if (isWriteObject()) return getMap(dictionaryItem);
        return getSingleValue(dictionaryItem);
    }

    protected JsonNode getJsonNode(DictionaryItem<?> dictItem) {
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
