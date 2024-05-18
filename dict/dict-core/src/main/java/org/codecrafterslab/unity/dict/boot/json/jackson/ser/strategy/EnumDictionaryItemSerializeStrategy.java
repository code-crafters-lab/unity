package org.codecrafterslab.unity.dict.boot.json.jackson.ser.strategy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

public abstract class EnumDictionaryItemSerializeStrategy implements SerializeStrategy<EnumDictItem<?>> {

    @Override
    public boolean support(EnumDictItem<?> dictItem) {
//        EnumDictItem.class.isAssignableFrom(dictItem.getClass());
        return true;
    }

    @Override
    public void serialize(EnumDictItem<?> dictItem, JsonGenerator gen, SerializerProvider serializerProvider) {

    }
}
