package org.codecrafterslab.unity.dict.api;

import java.util.Collection;

/**
 * @author Wu Yujie
 */
public interface Dictionary<V> {

    /**
     * 字典编码
     *
     * @return 编码
     */
    String getCode();

    /**
     * 字典名称
     *
     * @return 名称
     */
    String getName();

    /**
     * @return 字典项集合
     */
    <Item extends DictionaryItem<V>> Collection<Item> getItems();
}
