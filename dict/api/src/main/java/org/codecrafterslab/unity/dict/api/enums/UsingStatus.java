package org.codecrafterslab.unity.dict.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

/**
 * 数据使用状态,用于数据具有锁定功能的场景
 *
 * @author Wu Yujie
 * @since 0.3.0
 */
@Getter
@AllArgsConstructor
public enum UsingStatus implements EnumDictItem<Integer> {

    DISABLE(-1, "禁用", "长期不使用"),
    LOCK(0, "锁定", "临时不使用"),
    NORMAL(1, "正常", "正常使用");

    private final Integer value;
    private final String label;
    private final String description;


    @Override
    public String getDescription() {
        return description;
    }
}
