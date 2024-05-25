package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

public class DictBeanPropertyWriter extends BeanPropertyWriter {

    public DictBeanPropertyWriter(BeanPropertyWriter base, PropertyName name) {
        super(base, name);
    }
}
