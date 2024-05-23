package org.codecrafterslab.unity.dict.api.func;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.enums.Action;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WuYujie
 */
@Slf4j
class FunctionsTest {

    @Test
    public void functionsBuilder() {
        Functions functions = Functions.builder().of(15).build();
        assertEquals("15", functions.getFunctions());
        assertEquals(BigInteger.valueOf(15), functions.getSource());
        assertEquals(BigInteger.valueOf(15), functions.get());
    }

    @Test
    public void functionsActionBuilder() {
        BigInteger all = BigInteger.valueOf(31);

        Functions functions =
                Functions.builder()
                        .of(Action.EXPORT) // 初始化导出权限
                        .function(Action.ADD, Action.DELETE, Action.UPDATE, Action.QUERY) // 额外增删改查权限
                        .build();
        assertEquals("31", functions.getFunctions());
        assertEquals(all, functions.getSource());
        assertEquals(all, functions.get());
        assertTrue(functions.has(Action.ADD));
        assertTrue(functions.has(Action.DELETE));
        assertTrue(functions.has(Action.UPDATE));
        assertTrue(functions.has(Action.QUERY));

        /* 移除添加权限 */
        functions = functions.remove(Action.ADD);

        assertFalse(functions.has(Action.ADD));
        assertEquals(all, functions.getSource());
        assertEquals(all, functions.getSource());
        assertEquals(all.subtract(Action.ADD.get()), functions.get());

        /* 指定权限判断 */
        assertTrue(functions.hasAll(Action.QUERY, Action.UPDATE));
        assertTrue(functions.hasAny(Action.ADD, Action.DELETE));

        Functions removed = functions.remove(Stream.of(Action.QUERY, Action.ADD)
                .mapToInt(value -> value.get().intValue()).sum());
        assertTrue(functions.hasNone(Action.QUERY, Action.ADD));
        assertNotEquals(all.toString(), removed.getFunctions());

        functions = functions.remove(10);
        assertTrue(functions.has(Action.DELETE, Action.EXPORT));

        /* 重置权限 */
        functions.reset();
        assertEquals(all, functions.get());
    }

    @Test
    public void bigint() {
        BigInteger one = BigInteger.valueOf(15);
        BigInteger two = BigInteger.valueOf(6);

        // 移除权限 位运算 andNot &~
        BigInteger r1 = one.andNot(two);
        BigInteger r2 = two.andNot(one);

        assertEquals(BigInteger.valueOf(9), r1);
        assertEquals(BigInteger.ZERO, r2);

        // 添加权限使用 位运算 or
        assertEquals(BigInteger.valueOf(32).subtract(BigInteger.ONE), one.or(BigInteger.valueOf(16)));
        assertEquals(BigInteger.valueOf(79), one.or(BigInteger.valueOf(64)));

        // 删除权限使用  &~ ，先取反再按位与运算
        assertEquals(BigInteger.valueOf(9), one.andNot(two));
        assertEquals(BigInteger.ZERO, two.andNot(one));
    }
}
