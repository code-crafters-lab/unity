package org.codecrafterslab.unity.dict.api.base;

import org.codecrafterslab.unity.dict.api.enums.DataFlag;

/**
 * @author Wu Yujie
 * @since 0.3.0
 */
public interface IFlag {

    /**
     * 获取数据状态标识
     *
     * @return 状态标识
     */
    DataFlag getFlag();

    /**
     * 数据状态赋值
     *
     * @param dataFlag DataFlag
     */
    void setFlag(DataFlag dataFlag);

}
