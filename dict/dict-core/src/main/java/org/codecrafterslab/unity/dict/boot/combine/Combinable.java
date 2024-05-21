package org.codecrafterslab.unity.dict.boot.combine;


import org.springframework.lang.Nullable;

public interface Combinable<T> {
    T combine(@Nullable T other);
}
