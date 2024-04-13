package org.codecrafterslab.unity.exception.api;

public interface Module {

    /**
     * 是否内部模块
     *
     * @return false
     */
    boolean internal();

    /**
     * 模块唯一标识
     *
     * @return int
     */
    int getValue();

}
