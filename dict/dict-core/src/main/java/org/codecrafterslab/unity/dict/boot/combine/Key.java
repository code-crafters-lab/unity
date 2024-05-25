package org.codecrafterslab.unity.dict.boot.combine;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Getter
@ToString
@EqualsAndHashCode
public class Key implements Combinable<Key> {

    /**
     * 序列化字段范围（类型）
     */

    private final Scope scope;

    /**
     * 序列化属性名称
     */
    private String value;

    private Key(Scope scope, String value) {
        this.scope = scope;
        this.value = value;
    }

    private Key(Scope scope, @Nullable Map<Scope, String> config, boolean withDefault) {
        this(scope, config == null
                ? (withDefault ? scope.getCode().toLowerCase() : null)
                : (withDefault ? config.getOrDefault(scope, scope.getCode().toLowerCase()) : config.get(scope)));
    }

    public static Key of(Scope scope, String value) {
        return new Key(scope, value);
    }

    public static Key of(Scope scope, boolean withDefault) {
        return new Key(scope, null, withDefault);
    }

    public static Key of(Scope scope, DictSerialize anno, @Nullable Map<Scope, String> scopeKeyValues) {
        Key key = new Key(scope, scopeKeyValues, false);
        switch (scope) {
            case ID:
                return key.setValue(anno.id());
            case CODE:
                return key.setValue(anno.code());
            case VALUE:
            case LABEL:
                return key.setValue(anno.label());
            case SORT:
                return key.setValue(anno.sort());
            case DISABLED:
                return key.setValue(anno.disabled());
            case DESCRIPTION:
                return key.setValue(anno.description());
            default:
                return key;
        }
    }

    public static Key of(Scope scope, Map<Scope, String> config, boolean withDefault) {
        return new Key(scope, config, withDefault);
    }

    public static Key of(Scope scope, Map<Scope, String> config) {
        return of(scope, config, true);
    }

    public static Key of(Scope scope) {
        return of(scope, null, false);
    }

    public static List<Key> combine(Collection<Key> keys, Collection<Key> otherKeys) {
        return combine(keys, otherKeys, CombineStrategy.COVER);
    }

    public static List<Key> combine(Collection<Key> keys, Collection<Key> otherKeys, CombineStrategy strategy) {
        switch (strategy) {
            case MERGE:
                Map<Scope, List<Key>> groupedKeys = keys.stream()
                        .collect(Collectors.groupingBy(Key::getScope));

                for (Key key : otherKeys) {
                    groupedKeys.computeIfAbsent(key.getScope(), s -> new ArrayList<>()).add(key);
                }

                return groupedKeys.entrySet().stream()
                        .map(e -> e.getValue().stream().reduce(Key.of(e.getKey(), true), Key::combine))
                        .sorted(Comparator.comparing(Key::getScope))
                        .collect(Collectors.toList());
            case COVER:
            default:
                if (otherKeys instanceof List) {
                    return (List<Key>) otherKeys;
                } else if (otherKeys != null) {
                    return new ArrayList<>(otherKeys);
                } else if (keys instanceof List) {
                    return (List<Key>) keys;
                }
                return new ArrayList<>(keys);
        }

    }

    /**
     * 获取序列化属性名称
     * <p>
     * 如果{@link #value}不为空，则返回{@link #value}，否则返回{@link Scope#getCode()}的的小写形式
     * </p>
     *
     * @return String
     */
    public @NonNull String getValueWithDefault() {
        return StringUtils.hasText(value) ? value : scope.getCode().toLowerCase();
    }

    public Key setValue(String value) {
        if (StringUtils.hasText(value)) this.value = value;
        return this;
    }

    public <D extends DictionaryItem<?>> Object getDictionaryItemFiledValue(D dictItem) {
        return this.scope.getDictionaryItemFiledValue(dictItem);
    }

    @Override
    public Key combine(Key other, CombineStrategy strategy) {
        if (other != null && this.scope.equals(other.scope) && StringUtils.hasText(other.value) && !other.value.equals(this.value)) {
            return other;
        }
        return this;
    }
}
