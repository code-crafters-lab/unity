package org.codecrafterslab.unity.dict.boot.combine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public enum ScopesOutputMode implements FuncEnumDictItem {
    OBJECT(1, "对象输出"),
    FLAT(2, "扁平化输出"),
    FLAT_WITH_OBJECT(3, "扁平化输出且含对象"),
    ;

    private final Integer value;
    private final String name;

    @Override
    public BigInteger getValue() {
        return BigInteger.valueOf(value);
    }

}
