package org.codecrafterslab.unity.dict.api.persist.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.codecrafterslab.unity.dict.api.persist.DataDict;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wu Yujie
 * @since 0.2.0
 */
@Builder
@EqualsAndHashCode
public final class Dict<ID extends Serializable, V> implements DataDict<ID, V> {
    private ID id;
    private String code;
    private String name;
    private List<DictItem<ID, V>> items;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DictItem<ID, V>> getItems() {
        return items;
    }
}
