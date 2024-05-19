package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

public interface SerializeKey extends SerializeCondition<SerializeKey> {
    String getIdKey();

    String getCodeKey();

    String getValueKey();

    String getLabelKey();

    String getSortKey();

    String getDisabledKey();

    String getDescriptionKey();
}
