package org.codecrafterslab.unity.dict.boot.app.service;

import org.codecrafterslab.unity.dict.boot.app.entity.Sex;
import org.codecrafterslab.unity.dict.boot.app.entity.User;

public interface UserService {

    User getSexByStringValue(String value);

    User getSexByIntegerValue(Integer value);

    User getSexByCode(String code);

    User getSex(Sex sex);
}
