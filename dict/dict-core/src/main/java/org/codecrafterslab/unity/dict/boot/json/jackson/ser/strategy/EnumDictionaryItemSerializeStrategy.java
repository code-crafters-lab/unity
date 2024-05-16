package org.codecrafterslab.unity.dict.boot.json.jackson.ser.strategy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

public class EnumDictionaryItemSerializeStrategy implements SerializeStrategy {
    @Override
    public boolean support(DictionaryItem dictionaryItem) {
        return EnumDictItem.class.isAssignableFrom(dictionaryItem.getClass());
    }

    @Override
    public void serialize(DictionaryItem dictItem, JsonGenerator gen, SerializerProvider serializerProvider) {

    }
}
