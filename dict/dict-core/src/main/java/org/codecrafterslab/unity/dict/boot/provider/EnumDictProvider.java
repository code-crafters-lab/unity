package org.codecrafterslab.unity.dict.boot.provider;


import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WuYujie
 * @since 1.0.0
 */
@Getter
public class EnumDictProvider {

    /**
     * 枚举字典
     */
    private final List<Class<? extends EnumDictItem<?>>> enumDictItem;

    /**
     * 功能点枚举字段
     */
    private final List<Class<? extends FuncEnumDictItem>> funcEnumDictItem;

    public EnumDictProvider(Collection<Class<? extends EnumDictItem<?>>> collections) {
        enumDictItem = new ArrayList<>(collections);
        funcEnumDictItem = filter(enumDictItem);
    }

    @SuppressWarnings("unchecked")
    List<Class<? extends FuncEnumDictItem>> filter(List<Class<? extends EnumDictItem<?>>> list) {
        return list.stream()
                .filter(dict -> FuncEnumDictItem.class.isAssignableFrom(dict) && dict.isEnum())
                .map((Function<Class<? extends EnumDictItem<?>>, Class<? extends FuncEnumDictItem>>) aClass ->
                        (Class<? extends FuncEnumDictItem>) aClass)
                .collect(Collectors.toList());
    }
}
