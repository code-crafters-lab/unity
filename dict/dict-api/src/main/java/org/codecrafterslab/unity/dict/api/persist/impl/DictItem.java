package org.codecrafterslab.unity.dict.api.persist.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.codecrafterslab.unity.dict.api.persist.DataDictItem;

import java.io.Serializable;

/**
 * @author Wu Yujie
 * @since 0.2.0
 */
@Builder
@EqualsAndHashCode
public final class DictItem<ID extends Serializable, V> implements DataDictItem<ID, V> {
    private ID id;
    private V value;
    private String label;
    private String description;
    private String code;
    private Integer sort;
    private Boolean disabled;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public Integer getSort() {
        return sort;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Boolean isDisabled() {
        return disabled;
    }

//    @Override
//    public int compareTo(@Nullable DictItem<ID, V> other) {
//        if (other == null) return 1;
//        Integer sort1 = getSort();
//        Integer sort2 = other.getSort();
//        if (sort1 != null && sort2 != null) {
//            return sort1.compareTo(sort2);
//        } else {
//            // todo 比较还是有问题
//            if (sort1 == null && sort2 != null) {
//                return -1;
//            }
////            if (sort2 == null && sort2 = null) {
////                return 1;
////            }
//        }
//        return 0;
//    }
}
