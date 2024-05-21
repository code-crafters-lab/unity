package org.codecrafterslab.unity.dict.boot.combine;

import org.springframework.lang.Nullable;

public abstract class AbstractCombinable<T> implements Combinable<T> {

    // 获取当前实例的值
    @Nullable
    public abstract T getValue();

    // 抽象方法，接受两个可选的T类型参数value和other，返回一个新的T类型对象
    public abstract T combine(@Nullable T value, @Nullable T other);

    @Override
    public T combine(@Nullable T other) {
        // 获取当前实例的值
        T value = getValue();

        // 检查两个值是否都不为null
        if (value != null && other != null) {
            // 检查两个值的类型是否相同
            if (!value.getClass().equals(other.getClass())) {
                throw new IllegalArgumentException("不能将类型为 " + value.getClass() + " 和 " + other.getClass() + " " +
                        "的值组合在一起");
            }
            // 组合两个值
            return combine(value, other);
        }
        // 返回当前实例的值
        return value;
    }

}


