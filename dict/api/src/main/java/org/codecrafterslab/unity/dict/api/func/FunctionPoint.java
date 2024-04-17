package org.codecrafterslab.unity.dict.api.func;

import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * 功能点描述
 *
 * @author WuYujie
 * @since 0.1.2
 */
public interface FunctionPoint extends Supplier<BigInteger> {

    /**
     * 功能点名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 功能点位置，必须大于零，且不允许重复
     *
     * @return 位置索引
     */
    int getPosition();

    /**
     * 功能点描述
     *
     * @return 描述信息
     */
    String getDescription();

    /**
     * 权限大整数表示
     *
     * @return BigInteger
     */
    @Override
    default BigInteger get() {
        int position = getPosition();
        if (position <= 0) throw new RuntimeException("功能点位置 position 的值必须为正整数");
        return BigInteger.ONE.shiftLeft(position - 1);
    }

}
