package org.codecrafterslab.unity.dict.boot;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

/**
 * @author Wu Yujie
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum Sex implements EnumDictItem<Integer> {
    MALE(1, "男"),
    FEMALE(2, "女"),
    ;

    private final Integer value;
    private final String label;


}
