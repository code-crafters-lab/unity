package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import java.util.List;

public interface SerializeCondition extends SerializeKey {

    List<SerializeScope> getScopes();

//    SerializeCondition combine(SerializeCondition other);
}
