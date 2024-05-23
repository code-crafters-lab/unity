package org.codecrafterslab.unity.dict.boot.app.service.impl;

import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.app.entity.Sex;
import org.codecrafterslab.unity.dict.boot.app.entity.User;
import org.codecrafterslab.unity.dict.boot.app.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getSexByStringValue(String value) {
        Sex sex = EnumDictItem.find(Sex.class, value);
        return User.builder().id("1").name("demo1").sex(sex).valueSex(sex)
                .labelSex(sex).codeSex(sex).allSex(sex).build();
    }

    @Override
    public User getSexByIntegerValue(Integer value) {
        Sex sex = EnumDictItem.find(Sex.class, value);
        return User.builder().id("2").name("demo2").sex(sex).valueSex(sex)
                .labelSex(sex).codeSex(sex).allSex(sex).build();
    }

    @Override
    public User getSexByCode(String code) {
        Sex sex = EnumDictItem.find(Sex.class, code);
        return User.builder().id("3").name("demo3").sex(sex).valueSex(sex)
                .labelSex(sex).codeSex(sex).allSex(sex).build();
    }

    @Override
    public User getSex(Sex sex) {
        return User.builder().id("4").name("demo4").sex(sex).valueSex(sex)
                .labelSex(sex).codeSex(sex).allSex(sex).build();
    }
}
