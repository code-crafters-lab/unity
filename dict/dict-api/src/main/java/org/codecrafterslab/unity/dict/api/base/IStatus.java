package org.codecrafterslab.unity.dict.api.base;

import org.codecrafterslab.unity.dict.api.enums.UsingStatus;

/**
 * @author Wu Yujie
 * @since 0.3.0
 */
public interface IStatus {

    /**
     * 获取数据使用状态
     *
     * @return 状态标识
     */
    UsingStatus getStatus();

    /**
     * 数据使用状态赋值
     *
     * @param usingStatus UsingStatus
     */
    void setStatus(UsingStatus usingStatus);

}
