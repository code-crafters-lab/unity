package org.codecrafterslab.unity.dict.boot.provider;

import org.codecrafterslab.unity.dict.api.EnumDictItem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EnumDictProviderBuilder {
    private final Set<Class<? extends EnumDictItem<?>>> classes = new HashSet<>();

    public EnumDictProvider build() {
        return new EnumDictProvider(classes);
    }

    public void add(Class<? extends EnumDictItem<?>> clazz) {
        classes.add(clazz);
    }

    public void add(Collection<Class<? extends EnumDictItem<?>>> list) {
        classes.addAll(list);
    }

    public void reset() {
        classes.clear();
    }
}
