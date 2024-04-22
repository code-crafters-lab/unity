package org.codecrafterslab.unity.dict.api.base;

import java.io.Serializable;

/**
 * @author Wu Yujie
 * @since 0.3.0
 */
public interface Identify<ID extends Serializable> extends IBean {
    /**
     * 获取 id
     *
     * @return ID
     */
    ID getId();

    /**
     * 设置 id
     *
     * @param id ID
     */
    void setId(ID id);
}
