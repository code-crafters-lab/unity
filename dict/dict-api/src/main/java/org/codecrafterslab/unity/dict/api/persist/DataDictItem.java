package org.codecrafterslab.unity.dict.api.persist;


import org.codecrafterslab.unity.dict.api.DictionaryItem;

import java.io.Serializable;

/**
 * @author Wu Yujie
 * @since 0.2.0
 */
public interface DataDictItem<ID extends Serializable, V> extends DictionaryItem<V>, Identify<ID> {
}
