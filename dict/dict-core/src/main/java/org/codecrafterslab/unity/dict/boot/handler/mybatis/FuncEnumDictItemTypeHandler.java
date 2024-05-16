package org.codecrafterslab.unity.dict.boot.handler.mybatis;

import org.apache.ibatis.type.TypeHandlerRegistry;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.api.func.Functions;
import org.codecrafterslab.unity.dict.boot.json.ValuePersistence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    private final ValuePersistence.Mode mode = ValuePersistence.Mode.COMMA_SPLIT;

    public FuncEnumDictItemTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.type = type;
        }
    }

    @Override
    protected Object fromList(List<T> parameter) {
        switch (mode) {
            case COMMA_SPLIT:
                return parameter.stream().map(val -> val.getValue().toString()).collect(Collectors.joining(","));
            case ACCUMULATION:
            case AUTO:
            default:
                Functions functions = Functions.builder().functions(parameter).build();
                return functions.getFunctions();
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
                            .collect(Collectors.toList());
                    break;
                case ACCUMULATION:
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
