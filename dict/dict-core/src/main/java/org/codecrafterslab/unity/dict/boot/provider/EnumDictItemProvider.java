package org.codecrafterslab.unity.dict.boot.provider;


import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 提供实现了 {@link EnumDictItem} 接口的类
 *
 * @author WuYujie
 * @since 1.0.1
 */
@FunctionalInterface
public interface EnumDictItemProvider {

    Collection<Class<? extends EnumDictItem<?>>> get();

    class Builder {
        private final Set<Class<? extends EnumDictItem<?>>> classes;

        public Builder(ObjectProvider<EnumDictItemProvider> providers) {
            classes = providers.orderedStream().reduce(new HashSet<>(), (classes, funcEnumProvider) -> {
                classes.addAll(funcEnumProvider.get());
                return classes;
            }, (classes, classes2) -> classes);
        }

        public Collection<Class<? extends EnumDictItem<?>>> get() {
            return classes;
        }
    }
}
