package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

@Getter
@AllArgsConstructor
public enum SerializeScope implements EnumDictItem<Integer> {
    ID(1),
    CODE(2),
    VALUE(4),
    LABEL(8),
    SORT(16),
    DISABLED(32),
    DESCRIPTION(64),

    CODE_LABEL(10),
    VALUE_LABEL(12),
    CODE_VALUE_LABEL(14),
    ALL(127);

    private final Integer value;

    @Override
    public String getLabel() {
        return "";
    }
}
