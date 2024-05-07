package org.codecrafterslab.unity.dict.api;

import org.codecrafterslab.unity.dict.api.func.FunctionPoint;
import org.codecrafterslab.unity.dict.api.func.Functions;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * 功能点枚举
 *
 * @author WuYujie
 * @since 0.1.2
 */
public interface FuncEnumDictItem extends EnumDictItem<BigInteger>, FunctionPoint {

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

    /**
     * 获取功能点列表
     *
     * @param type      Class<T>
     * @param functions 功能点描述
     * @param <T>       枚举类型
     * @return List<T>
     */
    static <T extends FuncEnumDictItem> List<T> getFunctions(Class<T> type, Functions functions) {
        BigInteger func = functions.get();
        if (func != null) {
            return EnumDictItem.findByCondition(type, item -> item.get().or(func).equals(func),
                    FuncEnumDictItem.class::isAssignableFrom);
        }
        return Collections.emptyList();
    }


}
