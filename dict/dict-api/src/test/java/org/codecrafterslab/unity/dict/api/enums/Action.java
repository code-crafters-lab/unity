package org.codecrafterslab.unity.dict.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;

/**
 * @author WuYujie
 */
@Getter
@AllArgsConstructor
public enum Action implements FuncEnumDictItem {
    QUERY("查询"),
    ADD("添加"),
    DELETE("删除"),
    UPDATE("更新"),
    EXPORT("导出");

    private final String name;

}
