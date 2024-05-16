package org.codecrafterslab.unity.dict.api;

import org.codecrafterslab.unity.dict.api.func.FunctionPoint;
import org.codecrafterslab.unity.dict.api.func.Functions;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 功能点枚举
 *
 * @author WuYujie
 * @since 0.1.2
 */
public interface FuncEnumDictItem extends EnumDictItem<BigInteger>, FunctionPoint {

    /**
     * 获取功能点列表
     *
     * @param type      Class<T>
     * @param functions 功能点描述
     * @param <T>       枚举类型
     * @return List<T>
     * @see #find(Class, Object, Function)
     */
    static <T extends FuncEnumDictItem> List<T> find(Class<T> type, Functions functions) {
        return find(type, functions, val -> (Functions) val);
    }

    static <T extends FuncEnumDictItem> List<T> find(Class<T> type, Object functions,
                                                     Function<Object, Functions> convert) {
        BigInteger func = convert.apply(functions).get();
        if (func == null) return Collections.emptyList();
        return EnumDictItem.findByCondition(type, FuncEnumDictItem.class::isAssignableFrom,
                item -> item.get().or(func).equals(func)
        );
    }

    @Override
    default BigInteger getValue() {
        return get();
    }

    @Override
    default String getLabel() {
        return getName();
    }

    @Override
    default int getPosition() {
        return ordinal() + 1;
    }

    @Override
    default String getDescription() {
        return EnumDictItem.super.getDescription();
    }

}
