package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.Getter;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.api.func.Functions;
import org.codecrafterslab.unity.dict.api.persist.DataDictItem;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SerializeScope implements FuncEnumDictItem {
    ID(1, true), // 1
    CODE(1 << 1, true), // 2
    VALUE(1 << 2, true), // 4
    LABEL(1 << 3, true), // 8
    SORT(1 << 4, true), // 16
    DISABLED(1 << 5, true), // 32
    DESCRIPTION(1 << 6, true), // 64

    CODE_LABEL(1 << 1 | 1 << 3, false), // 10
    VALUE_LABEL(1 << 2 | 1 << 3, false), // 12
    CODE_VALUE_LABEL(1 << 1 | 1 << 2 | 1 << 3, false), // 14

    ALL(1 << 7 - 1, false);

    private static List<SerializeScope> serializeScopes;
    private final Integer value;
    /**
     * 仅用于内部逻辑判读
     */
    @Getter
    private final boolean inner;

    SerializeScope(Integer value, boolean inner) {
        this.value = value;
        this.inner = inner;
    }

    /**
     * 查询内部使用的 SerializeScope
     *
     * @return List<SerializeScope>
     */
    static List<SerializeScope> findInnerSerializeScope() {
        if (serializeScopes == null) {
            serializeScopes = Arrays.stream(SerializeScope.values())
                    .filter(SerializeScope::isInner).collect(Collectors.toList());
        }
        return serializeScopes;
    }

    /**
     * 在指定的范围查询内部使用的 SerializeScope
     *
     * @param all Collection<SerializeScope>
     * @return List<SerializeScope>
     */
    static List<SerializeScope> findInnerSerializeScope(Collection<SerializeScope> all) {
        return all.stream().flatMap((Function<SerializeScope, Stream<SerializeScope>>)
                scope -> {
                    Functions functions;
                    if (!scope.isInner()) {
                        functions = Functions.builder().of(scope.getValue()).build();
                        return findInnerSerializeScope().stream().filter(functions::has);
                    }
                    return Stream.of(scope);
                }
        ).distinct().collect(Collectors.toList());
    }

    @Override
    public BigInteger getValue() {
        return BigInteger.valueOf(value);
    }

    @Override
    public String getName() {
        return value.toString();
    }

    /**
     * 获取序列化的 key 值
     *
     * @param keys SerializeKey
     * @return String
     */
    public String getKey(SerializeKey keys) {
        switch (this) {
            case ID:
                return keys.getIdKey();
            case CODE:
                return keys.getCodeKey();
            case VALUE:
                return keys.getValueKey();
            case LABEL:
                return keys.getLabelKey();
            case SORT:
                return keys.getSortKey();
            case DISABLED:
                return keys.getDisabledKey();
            case DESCRIPTION:
                return keys.getDescriptionKey();
            default:
                return null;
        }
    }

    /**
     * 获取序列号 value 值
     *
     * @param dictItem DictionaryItem<?>
     * @return Object
     */
    public <D extends DictionaryItem<?>> Object getValue(D dictItem) {
        switch (this) {
            case ID:
                Serializable id = null;
                if (DataDictItem.class.isAssignableFrom(dictItem.getClass())) {
                    id = ((DataDictItem<?, ?>) dictItem).getId();
                }
                return id;
            case CODE:
                return dictItem.getCode();
            case VALUE:
                return dictItem.getValue();
            case LABEL:
                return dictItem.getLabel();
            case SORT:
                return dictItem.getSort();
            case DISABLED:
                return dictItem.isDisabled();
            case DESCRIPTION:
                return dictItem.getDescription();
            default:
                return null;
        }
    }
}
