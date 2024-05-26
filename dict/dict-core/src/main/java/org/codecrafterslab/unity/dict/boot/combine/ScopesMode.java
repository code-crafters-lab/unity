package org.codecrafterslab.unity.dict.boot.combine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public enum ScopesMode implements FuncEnumDictItem {
    NO_OP(1, "不操作处理，使用原始对象输出"),
    FLAT(2, "扁平化输出属性"),
    FLAT_WITH_OBJECT(3, "扁平化输出属性且含原始输出"),
    ;

    private final Integer value;
    private final String name;

    @Override
    public BigInteger getValue() {
        return BigInteger.valueOf(value);
    }

}
