package org.codecrafterslab.unity.dict.boot.combine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.api.persist.DataDictItem;

import java.io.Serializable;

/**
 * 字典相关字段配置
 */
@Getter
@AllArgsConstructor
public enum Scope implements EnumDictItem<Integer> {
    ID(1, "持久化唯一标识"),
    CODE(2, "编码"),
    VALUE(4, "实际值"),
    LABEL(8, "显示值"),
    SORT(16, "排序"),
    DISABLED(32, "禁用状态"),
    DESCRIPTION(64, "描述"),
    ;

    private final Integer value;
    private final String label;

    /**
     * 获取 scope 对应序列化 value 值
     *
     * @param dictItem DictionaryItem<?>
     * @return Object
     */
    public <D extends DictionaryItem<?>> Object getDictionaryItemFiledValue(D dictItem) {
        switch (this) {
            case ID:
                Serializable id = null;
                if (DataDictItem.class.isAssignableFrom(dictItem.getClass())) {
                    id = ((DataDictItem<?, ?>) dictItem).getId();
                }
                return id;
            case CODE:
                return dictItem.getCode();
            case VALUE:
                return dictItem.getValue();
            case LABEL:
                return dictItem.getLabel();
            case SORT:
                return dictItem.getSort();
            case DISABLED:
                return dictItem.isDisabled();
            case DESCRIPTION:
                return dictItem.getDescription();
            default:
                return null;
        }
    }

}
