package org.codecrafterslab.unity.dict.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

/**
 * 数据标识（增删改标识）
 *
 * @author Wu Yujie
 * @since 0.3.0
 */
@Getter
@AllArgsConstructor
public enum DataFlag implements EnumDictItem<Integer> {

    DELETE(-1, "删除"),
    MODIFY(0, "修改"),
    ADD(1, "新增"),
    UNKNOWN(2, "未知");

    private final Integer value;
    private final String label;

}
