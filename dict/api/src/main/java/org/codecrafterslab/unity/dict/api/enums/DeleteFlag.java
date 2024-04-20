package org.codecrafterslab.unity.dict.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

/**
 * 逻辑删除标识
 *
 * @author Wu Yujie
 * @since 0.3.0
 */
@Getter
@AllArgsConstructor
public enum DeleteFlag implements EnumDictItem<Integer> {

    NO(1, "否", "未删除数据标识"),
    YES(0, "是", "已删除数据标识");

    private final Integer value;
    private final String label;
    private final String description;

    @Override
    public String getDescription() {
        return description;
    }
}
