package org.codecrafterslab.unity.dict.boot;

import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.exception.core.BizException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SexTest {

    @Test
    void sex() {
        Sex sex1 = EnumDictItem.findByValue(Sex.class, 1);
        Assertions.assertEquals(Sex.MALE, sex1);

        Sex sex2 = EnumDictItem.findByValue(Sex.class, "1");
        Assertions.assertEquals(Sex.MALE, sex2);

        Sex sex3 = EnumDictItem.findByCode(Sex.class, "male");
        Assertions.assertEquals(Sex.MALE, sex3);

        Sex sex4 = EnumDictItem.findByCode(Sex.class, "MALE");
        Assertions.assertEquals(Sex.MALE, sex4);

        Sex sex5 = EnumDictItem.findByValue(Sex.class, "4");
        Assertions.assertNull(sex5);

        Sex sex6 = EnumDictItem.findByCode(Sex.class, "4");
        Assertions.assertNull(sex6);

        Assertions.assertThrows(BizException.class, () -> {
            Sex sex = EnumDictItem.find(Sex.class, "4");
        });
    }

}