package org.codecrafterslab.unity.dict.api;

import org.codecrafterslab.unity.dict.api.base.ICode;
import org.codecrafterslab.unity.dict.api.base.ISort;

/**
 * 数据字典项接口
 *
 * @param <V> 字典值泛型
 * @author Wu Yujie
 */
public interface DictionaryItem<V> extends ICode, ISort {
    /**
     * 字典项编码
     *
     * @return String
     * @since 0.1.4
     */
    String getCode();
    
    /**
     * 字典项实际值
     *
     * @return Value
     */
    V getValue();

    /**
     * 字典项显示值
     *
     * @return String
     */
    String getLabel();

    /**
     * 排序
     *
     * @return 排序
     */
    Integer getSort();

    /**
     * 字典项描述
     *
     * @return String
     */
    String getDescription();

    /**
     * 字典项项是否禁用
     *
     * @return Boolean
     * @since 0.1.4
     */
    default Boolean isDisabled() {
        return false;
    }

}
