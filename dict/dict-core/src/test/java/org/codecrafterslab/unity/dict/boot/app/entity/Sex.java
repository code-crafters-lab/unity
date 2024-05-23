package org.codecrafterslab.unity.dict.boot.app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

/**
 * @author Wu Yujie
 */
@Getter
@AllArgsConstructor
public enum Sex implements EnumDictItem<Integer> {
    MALE(1, "男"),
    FEMALE(2, "女"),
    ;

    private final Integer value;
    private final String label;


}
