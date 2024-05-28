package org.codecrafterslab.unity.dict.boot.provider;


import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;

import java.util.Collection;
import java.util.Collections;
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
    private final List<Class<? super EnumDictItem<?>>> enumDictItem;

    /**
     * 功能点枚举字段
     */
    private final List<Class<? super FuncEnumDictItem>> funcEnumDictItem;

    public EnumDictProvider(Collection<Class<? super EnumDictItem<?>>> collections, Collection<Class<?
            extends EnumDictItem<?>>> excludes) {
        enumDictItem = collections.stream().filter(aClass -> !excludes.contains(aClass)).collect(Collectors.toList());
        funcEnumDictItem = filter(enumDictItem);
    }

    public EnumDictProvider(Collection<Class<? super EnumDictItem<?>>> collections) {
        this(collections, Collections.emptyList());
    }

    List<Class<? super FuncEnumDictItem>> filter(List<Class<? super EnumDictItem<?>>> list) {
        return list.stream()
                .filter(dict -> FuncEnumDictItem.class.isAssignableFrom(dict) && dict.isEnum())
                .map((Function<Class<? super EnumDictItem<?>>, Class<? super FuncEnumDictItem>>) aClass ->
                        (Class<? super FuncEnumDictItem>) aClass)
                .collect(Collectors.toList());
    }
}
