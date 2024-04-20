package org.codecrafterslab.unity.dict.api.base;

import java.time.Instant;

/**
 * @author Wu Yujie
 * @since 0.3.0
 */
public interface IUpdate {
    /**
     * 获取更新人
     *
     * @return String
     */
    String getUpdatedBy();

    /**
     * 设置更新人
     *
     * @param updater 更新人 ID
     */
    void setUpdatedBy(String updater);

    /**
     * 获取更新时间
     *
     * @return Instant
     */
    Instant getUpdatedAt();

    /**
     * 设置更新时间
     *
     * @param time 更新时间
     */
    void setUpdatedAt(Instant time);

}
