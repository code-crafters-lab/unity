package org.codecrafterslab.unity.dict.api.base;

import java.time.Instant;

/**
 * @author Wu Yujie
 * @since 0.3.0
 */
public interface ICreate {
    /**
     * 获取创建人
     *
     * @return String
     */
    String getCreatedBy();

    /**
     * 设置创建人
     *
     * @param creator 创建人ID
     */
    void setCreatedBy(String creator);

    /**
     * 获取创建时间
     *
     * @return Instant
     */
    Instant getCreatedAt();

    /**
     * 设置创建时间
     *
     * @param time 创建时间
     */
    void setCreatedAt(Instant time);

}
