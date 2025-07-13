package org.codecrafterslab.unity.dict.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnumDictItemFinder implements DictionaryItemFinder {
    @Override
    public <T extends DictionaryItem<?>> Predicate<Class<T>> typePredicate() {
        return tClass -> tClass.isEnum() && EnumDictItem.class.isAssignableFrom(tClass);
    }

    @Override
    public <T extends DictionaryItem<?>> List<T> findByCondition(Class<T> type, Predicate<Class<T>> typePredicate, Predicate<T> predicate) {
        if (!typePredicate.test(type)) return Collections.emptyList();
        return Arrays.stream(type.getEnumConstants()).filter(predicate).collect(Collectors.toList());
    }
}
