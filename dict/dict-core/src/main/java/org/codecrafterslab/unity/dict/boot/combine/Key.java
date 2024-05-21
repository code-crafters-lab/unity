package org.codecrafterslab.unity.dict.boot.combine;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode
public class Key implements Combinable<Key> {
    private final Scope scope;
    private final String value;

    private Key(Scope scope, String value) {
        this.scope = scope;
        this.value = value;
    }

    private Key(Scope scope, boolean withDefault) {
        this(scope, withDefault ? scope.getCode().toLowerCase() : null);
    }

    private Key(Scope scope) {
        this(scope, false);
    }

    private Key(Scope scope, Map<Scope, String> config, boolean withDefault) {
        this(scope, config == null
                ? (withDefault ? scope.getCode().toLowerCase() : null)
                : (withDefault ? config.getOrDefault(scope, scope.getCode().toLowerCase()) : config.get(scope)));
    }

    private Key(Scope scope, Map<Scope, String> valueMap) {
        this(scope, valueMap, false);
    }

    public static Key of(Scope scope, String value) {
        return new Key(scope, value);
    }

    public static Key of(Scope scope, boolean withDefault) {
        return new Key(scope, withDefault);
    }

    public static Key of(Scope scope) {
        return new Key(scope, false);
    }

    public static Key of(Scope scope, Map<Scope, String> config, boolean withDefault) {
        return new Key(scope, config, withDefault);
    }

    public static Key of(Scope scope, Map<Scope, String> config) {
        return new Key(scope, config, false);
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
                if (otherKeys != null) return new ArrayList<>(otherKeys);
                return new ArrayList<>(keys);
        }

    }

    public String getValueWithDefault() {
        return StringUtils.hasText(value) ? value : scope.name().toLowerCase();
    }

    public <D extends DictionaryItem<?>> Object getDictionaryItemFiledValue(D dictItem) {
        return this.scope.getDictionaryItemFiledValue(dictItem);
    }

    @Override
    public Key combine(Key other) {
        if (other != null && this.scope.equals(other.scope) && StringUtils.hasText(other.value) && !other.value.equals(this.value)) {
            return other;
        }
        return this;
    }
}
