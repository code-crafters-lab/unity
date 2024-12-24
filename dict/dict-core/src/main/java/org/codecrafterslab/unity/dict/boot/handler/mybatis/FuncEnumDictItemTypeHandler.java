package org.codecrafterslab.unity.dict.boot.handler.mybatis;

import org.apache.ibatis.type.TypeHandlerRegistry;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.api.func.Functions;
import org.codecrafterslab.unity.dict.boot.PersistenceMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * mybatis EnumDictItem 枚举类型处理器
 *
 * @author Wu Yujie
 * @see FuncEnumDictItem
 * @see TypeHandlerRegistry#register(Class)
 */
public class FuncEnumDictItemTypeHandler<T extends FuncEnumDictItem> extends ListTypeHandler<T> {
    private final Class<T> type;
    private final PersistenceMode mode;

    public FuncEnumDictItemTypeHandler(Class<T> type) {
        this(type, PersistenceMode.OR_OPERATION);
    }

    public FuncEnumDictItemTypeHandler(Class<T> type, PersistenceMode mode) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.mode = mode;
    }

    @Override
    protected Object fromList(List<T> parameter) {
        switch (mode) {
            case COMMA_SPLIT:
                return parameter.stream().map(val -> val.getValue().toString()).collect(Collectors.joining(","));
            case OR_OPERATION:
            case AUTO:
            default:
                return Functions.builder().functions(parameter).build().getFunctions();
        }
    }

    @Override
    protected List<T> toList(Object value) {
        List<T> result;
        if (value instanceof String) {
            switch (mode) {
                case COMMA_SPLIT:
                    result = Arrays.stream(((String) value).split(","))
                            .map(val -> EnumDictItem.findByValue(type, val))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    break;
                case OR_OPERATION:
                case AUTO:
                default:
                    result = FuncEnumDictItem.find(type, value, val -> Functions.builder().of((String) val).build());
            }
        } else {
            result = Collections.emptyList();
        }
        return result;
    }

}
