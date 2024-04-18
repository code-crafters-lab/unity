package org.codecrafterslab.unity.dict.api.persist;

import java.io.Serializable;

/**
 * @author Wu Yujie
 */
public interface Identify<ID extends Serializable> extends Serializable {
    /**
     * 获取 id
     *
     * @return ID
     */
    ID getId();
}
