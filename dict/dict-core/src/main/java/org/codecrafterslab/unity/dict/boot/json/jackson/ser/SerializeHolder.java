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

    private final List<Key> keys;

    public SerializeHolder() {
        this(Collections.emptyList());
    }

    public SerializeHolder(List<Key> keys) {
        this.keys = keys;
    }

    private SerializeHolder(List<SerializeScope> serializeScopes, Map<Scope, String> keysMap, boolean withDefault) {
        this(SerializeScope.findScopes(serializeScopes)
                .stream().map(scope -> Key.of(scope, keysMap, withDefault))
                .collect(Collectors.toList())
        );
    }

    public SerializeHolder(List<SerializeScope> serializeScopes, Map<Scope, String> keysMap) {
        this(serializeScopes, keysMap, false);
    }

    public SerializeHolder(DictSerializeProperties properties) {
        this(properties.getScopes(), properties.getKeys(), true);
    }

    public SerializeHolder(SerializeScope[] serializeScopes) {
        this(Arrays.asList(serializeScopes), null, false);
    }

//    public SerializeHolder(SerializeHolder base, DictSerialize... dictSerialize) {
//        SerializeHolder other = SerializeHolder.of(dictSerialize);
//        if (base != null) {
//            other = base.combine(other);
//        }
//        if (other != base) {
//            this.keys = other.keys;
//        } else {
//            this.keys = base.keys;
//        }
//    }

    public static SerializeHolder of(DictSerialize... conditions) {
        return Arrays.stream(conditions)
                .filter(Objects::nonNull)
                .map(anno -> AnnotationUtils.synthesizeAnnotation(anno, DictSerialize.class))
                .map(anno -> new SerializeHolder(anno.scopes())).reduce(new SerializeHolder(),
                        SerializeHolder::combine);
    }

    public static SerializeHolder of(List<DictSerialize> dictSerializes) {
        return of(dictSerializes.toArray(new DictSerialize[]{}));
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
        keys.forEach(key -> data.put(key.getValueWithDefault(), key.getScope().getDictionaryItemFiledValue(dictItem)));
        return data;
    }

    @Override
    public SerializeHolder combine(SerializeHolder other) {
        if (!this.equals(other) && other != null) {
            List<Key> keys = Key.combine(this.keys, other.keys);
            if (!this.keys.equals(keys)) {
                return new SerializeHolder(keys);
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
                String value1 = key.getValue();
                Object value = key.getDictionaryItemFiledValue(dictItem);
//                objectNode.put(key.getValue(), value);
            });
            return objectNode;
        } else {
            return jsonNodeFactory.textNode("");
        }

    }
}
