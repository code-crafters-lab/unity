package org.codecrafterslab.unity.dict.api.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wu Yujie
 * @since 0.3.0
 */
public interface ITree<ID extends Serializable> extends Identify<ID> {

    /**
     * 获取 id
     *
     * @return ID
     */
    ID getParentId();

    /**
     * 设置 id
     *
     * @param id ID
     */
    void setParentId(ID id);

    default <C extends ITree<ID>> List<C> getChildren() {
        return null;
    }

}
