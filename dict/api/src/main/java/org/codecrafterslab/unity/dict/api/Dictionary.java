package org.codecrafterslab.unity.dict.api;

import org.codecrafterslab.unity.dict.api.base.ICodeName;

import java.util.Collection;

/**
 * @author Wu Yujie
 */
public interface Dictionary<V> extends ICodeName {

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

    interface Query extends ICodeName {
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
         * 是否包含字典项
         *
         * @return boolean
         */
        boolean isContainsItems();
    }
}
