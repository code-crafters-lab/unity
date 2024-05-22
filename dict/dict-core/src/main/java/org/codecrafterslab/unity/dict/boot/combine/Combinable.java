package org.codecrafterslab.unity.dict.boot.combine;


import org.springframework.lang.Nullable;

public interface Combinable<T> {

    default T combine(@Nullable T other) {
        return combine(other, CombineStrategy.COVER);
    }

    T combine(@Nullable T other, CombineStrategy strategy);
}
