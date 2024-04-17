package org.codecrafterslab.unity.dict.api.func.impl;

import org.codecrafterslab.unity.dict.api.func.FunctionPoint;
import org.codecrafterslab.unity.dict.api.func.Functions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author WuYujie
 */
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
    public boolean has(FunctionPoint point) {
        return functions != null && point.get().and(functions).equals(point.get());
    }

    @Override
    public boolean hasAll(FunctionPoint point, FunctionPoint... others) {
        return has(point) && Arrays.stream(others).allMatch(this::has);
    }

    @Override
    public boolean hasAny(FunctionPoint point, FunctionPoint... others) {
        return has(point) || Arrays.stream(others).anyMatch(this::has);
    }

    @Override
    public boolean hasNone(FunctionPoint point, FunctionPoint... others) {
        return !has(point) && Arrays.stream(others).noneMatch(this::has);
    }

    @Override
    public Functions add(Collection<FunctionPoint> points) {
        this.functions = points.stream()
                .filter(point -> !has(point))
                .reduce(functions, (bigInteger, point) -> bigInteger.add(point.get()), BigInteger::add);
        return this;
    }

    @Override
    public Functions add(FunctionPoint point, FunctionPoint... others) {
        List<FunctionPoint> functionPoints = getFunctionPoints(point, others);
        return this.add(functionPoints);
    }

    @Override
    public Functions remove(Collection<FunctionPoint> points) {
        this.functions = points.stream()
                .filter(this::has)
                .reduce(functions, (bigInteger, point) -> bigInteger.subtract(point.get()), BigInteger::divide);
        return this;
    }


    @Override
    public Functions remove(FunctionPoint point, FunctionPoint... others) {
        List<FunctionPoint> functionPoints = this.getFunctionPoints(point, others);
        return this.remove(functionPoints);
    }

    private List<FunctionPoint> getFunctionPoints(FunctionPoint point, FunctionPoint[] others) {
        List<FunctionPoint> functionPoints = new ArrayList<>();
        functionPoints.add(point);
        functionPoints.addAll(Arrays.asList(others));
        return functionPoints;
    }

}
