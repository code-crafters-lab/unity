package org.codecrafterslab.unity.dict.boot.json.jackson.ser.strategy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.codecrafterslab.unity.dict.api.DictionaryItem;

public interface SerializeStrategy {

    boolean support(DictionaryItem dictionaryItem);

    void serialize(DictionaryItem dictItem, JsonGenerator gen, SerializerProvider serializerProvider);
}
