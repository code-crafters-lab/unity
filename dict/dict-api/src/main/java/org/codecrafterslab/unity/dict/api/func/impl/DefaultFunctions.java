package org.codecrafterslab.unity.dict.api.func.impl;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.api.func.FunctionPoint;
import org.codecrafterslab.unity.dict.api.func.Functions;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author WuYujie
 */
@Slf4j
public final class DefaultFunctions implements Functions {
    /**
     * 原始权限大整数
     */
    private final BigInteger source;
    private BigInteger functions;

    public DefaultFunctions(BigInteger functions) {
        this.source = functions;
        this.functions = functions;
    }

    private static Stream<FunctionPoint> getFunctionPointStream(FunctionPoint point, FunctionPoint[] points) {
        return Stream.concat(Stream.of(point), Arrays.stream(points));
    }

    private static Stream<BigInteger> getBigIntegerStream(Stream<FunctionPoint> point) {
        return point.parallel().map(FunctionPoint::get);
    }

    @Override
    public BigInteger getSource() {
        return source;
    }

    @Override
    public BigInteger get() {
        return functions;
    }

    @Override
    public void reset() {
        this.functions = this.source;
    }

    @Override
    public boolean has(FunctionPoint point, FunctionPoint... points) {
        return getBigIntegerStream(getFunctionPointStream(point, points))
                .allMatch(value -> value.and(functions).equals(value));
    }

    @Override
    public boolean hasAll(FunctionPoint point, FunctionPoint... others) {
        return has(point, others);
    }

    @Override
    public boolean hasAny(FunctionPoint point, FunctionPoint... points) {
        return getBigIntegerStream(getFunctionPointStream(point, points))
                .anyMatch(value -> value.and(functions).equals(value));
    }

    @Override
    public boolean hasNone(FunctionPoint point, FunctionPoint... points) {
        return getBigIntegerStream(getFunctionPointStream(point, points))
                .noneMatch(value -> value.and(functions).equals(value));
    }

    @Override
    public Functions add(BigInteger bigInteger) {
        this.functions = this.functions.or(bigInteger);
        return this;
    }

    @Override
    public Functions add(String value) {
        return this.add(new BigInteger(value, 10));
    }

    @Override
    public Functions add(long longValue) {
        return this.add(BigInteger.valueOf(longValue));
    }

    @Override
    public Functions add(int intValue) {
        return this.add((long) intValue);
    }

    @Override
    public Functions add(Collection<FunctionPoint> points) {
        this.functions = getBigIntegerStream(points.stream()).reduce(functions, BigInteger::or);
        return this;
    }

    @Override
    public Functions add(FunctionPoint point, FunctionPoint... others) {
        this.functions = getBigIntegerStream(getFunctionPointStream(point, others))
                .reduce(functions, BigInteger::or);
        return this;
    }

    @Override
    public Functions remove(BigInteger bigInteger) {
        this.functions = this.functions.andNot(bigInteger);
        return this;
    }

    @Override
    public Functions remove(String value) {
        return remove(new BigInteger(value, 10));
    }

    @Override
    public Functions remove(long longValue) {
        return remove(BigInteger.valueOf(longValue));
    }

    @Override
    public Functions remove(int intValue) {
        return remove((long) intValue);
    }

    @Override
    public Functions remove(Collection<FunctionPoint> points) {
        this.functions = getBigIntegerStream(points.stream()).reduce(functions, BigInteger::andNot);
        return this;
    }

    @Override
    public Functions remove(FunctionPoint point, FunctionPoint... points) {
        this.functions = getBigIntegerStream(getFunctionPointStream(point, points))
                .reduce(functions, BigInteger::andNot);
        return this;
    }

    @Override
    public <T extends FuncEnumDictItem> Collection<T> resolveFuncEnum(Class<T> clazz) {
        return FuncEnumDictItem.find(clazz, this);
    }

}
