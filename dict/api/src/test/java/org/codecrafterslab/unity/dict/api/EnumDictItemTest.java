package org.codecrafterslab.unity.dict.api;

import org.codecrafterslab.unity.dict.api.enums.Sex;
import org.codecrafterslab.unity.exception.core.BizException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

class EnumDictItemTest {

    @Test
    void findByCondition() {
        List<Sex> sexes = EnumDictItem.findByCondition(Sex.class, item -> item.getValue().equals(1));
        Assertions.assertEquals(1, sexes.size());
        Assertions.assertEquals(Sex.MALE, sexes.get(0));
    }

    @Test
    void findAll() {
        List<Sex> sexes = EnumDictItem.findAll(Sex.class);
        Assertions.assertEquals(2, sexes.size());
        Assertions.assertEquals(Sex.MALE, sexes.get(0));
        Assertions.assertEquals(Sex.FEMALE, sexes.get(1));
    }

    @Test
    void getValueType() {
        Class<?> valueType = EnumDictItem.getValueType(Sex.class);
        Assertions.assertEquals(Integer.class, valueType);
        Assertions.assertNotEquals(String.class, valueType);
    }

    @Test
    void findByValue() {
        Sex sex1 = EnumDictItem.findByValue(Sex.class, 1);
        Assertions.assertEquals(Sex.MALE, sex1);

        Sex sex2 = EnumDictItem.findByValue(Sex.class, "1");
        Assertions.assertEquals(Sex.MALE, sex2);
    }

    @Test
    void findByValueNotFound() {
        Sex sex = EnumDictItem.findByValue(Sex.class, 3);
        Assertions.assertNull(sex);
    }

    @ParameterizedTest
    @ValueSource(strings = {"male", "MALE", "mALe"})
    void findByCode(String code) {
        Sex sex = EnumDictItem.findByCode(Sex.class, code);
        Assertions.assertEquals(Sex.MALE, sex);
    }

    @Test
    void findByCodeNotFound() {
        Sex sex = EnumDictItem.findByValue(Sex.class, "unknown");
        Assertions.assertNull(sex);
    }

    @Test
    void find() {
        Assertions.assertThrows(BizException.class, () -> {
            Sex sex = EnumDictItem.find(Sex.class, "4");
        });
    }
}