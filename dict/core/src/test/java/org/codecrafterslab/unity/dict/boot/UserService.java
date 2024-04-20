package org.codecrafterslab.unity.dict.boot;

public interface UserService {

    User getSexByStringValue(String value);

    User getSexByIntegerValue(Integer value);

    User getSexByCode(String code);

    User getSex(Sex sex);
}
