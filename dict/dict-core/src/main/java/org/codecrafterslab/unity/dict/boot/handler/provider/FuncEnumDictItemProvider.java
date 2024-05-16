package org.codecrafterslab.unity.dict.boot.handler.provider;


import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author WuYujie
 * @since 1.0.0
 */
@FunctionalInterface
public interface FuncEnumDictItemProvider {

    Collection<Class<? extends FuncEnumDictItem>> get();

    class Builder {
        private final Set<Class<? extends FuncEnumDictItem>> classes;

        public Builder(ObjectProvider<FuncEnumDictItemProvider> providers) {
            classes = providers.orderedStream().reduce(new HashSet<>(), (classes, funcEnumProvider) -> {
                classes.addAll(funcEnumProvider.get());
                return classes;
            }, (classes, classes2) -> classes);
        }

        public Collection<Class<? extends FuncEnumDictItem>> get() {
            return classes;
        }
    }
}
