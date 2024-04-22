package org.codecrafterslab.unity.dict.api.persist;


import org.codecrafterslab.unity.dict.api.Dictionary;

import java.io.Serializable;

public interface DataDict<ID extends Serializable, V> extends Dictionary<V>, Identify<ID> {

}
