package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

public interface SerializeCondition<T> {

    T combine(T other);
}
