package org.codecrafterslab.unity.dict.api.func;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.enums.Action;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
                        .of(Action.EXPORT.get()) // 初始化导出权限
                        .functions(Action.ADD, Action.DELETE, Action.UPDATE, Action.QUERY) // 额外增删改查权限
                        .build();
        assertEquals("31", functions.getFunctions());
        assertEquals(all, functions.getSource());
        assertEquals(all, functions.get());
        assertTrue(functions.has(Action.ADD));
        assertTrue(functions.has(Action.DELETE));
        assertTrue(functions.has(Action.UPDATE));
        assertTrue(functions.has(Action.QUERY));

        /* 移除添加权限 */
        functions.remove(Action.ADD);
        assertTrue(functions.hasNone(Action.ADD));
        assertEquals(all, functions.getSource());
        assertEquals(all.subtract(Action.ADD.get()), functions.get());

        /* 指定权限判断 */
        assertTrue(functions.hasAll(Action.QUERY, Action.UPDATE));
        assertTrue(functions.hasAny(Action.ADD, Action.DELETE));

        /* 重置权限 */
        functions.reset();
        assertEquals(all, functions.get());
    }
}
