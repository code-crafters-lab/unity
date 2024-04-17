package org.codecrafterslab.unity.dict.api.func;

import org.codecrafterslab.unity.dict.api.func.impl.DefaultFunctions;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

/**
 * 功能点描述（容器）
 *
 * @author WuYujie
 * @since 0.1.2
 */
public interface Functions extends Supplier<BigInteger> {

    /**
     * 原始功能点
     *
     * @return BigInteger
     */
    BigInteger getSource();

    /**
     * 重置功能点
     */
    void reset();

    /**
     * 获取功能点字符串描述
     *
     * @return 功能字符串
     */
    default String getFunctions() {
        return getFunctions(10);
    }

    /**
     * 获取功能点字符串描述
     *
     * @param base 字符串表示形式的基数
     * @return 功能字符串
     */
    default String getFunctions(Integer base) {
        return get().toString(base);
    }

    /**
     * 是否含有某项功能
     *
     * @param point 功能点
     * @return 是否包含指定功能点
     */
    boolean has(FunctionPoint point);

    /**
     * 是否含有所有功能
     *
     * @param point  功能点
     * @param others 其他可选功能点
     * @return 是否包含所有指定的功能点
     */
    boolean hasAll(FunctionPoint point, FunctionPoint... others);

    /**
     * 是否含任何一项功能
     *
     * @param point  功能点
     * @param others 其他可选功能点
     * @return 是否包含任何一项功能点
     */
    boolean hasAny(FunctionPoint point, FunctionPoint... others);

    /**
     * 任何一项功能都没有
     *
     * @param point  功能点
     * @param others 其他可选功能点
     * @return 是否包不包含所有的功能点
     */
    boolean hasNone(FunctionPoint point, FunctionPoint... others);

    /**
     * 添加功能点
     *
     * @param points 功能点集合
     * @return 功能点容器对象
     */
    Functions add(Collection<FunctionPoint> points);

    /**
     * 添加功能点
     *
     * @param point  功能点
     * @param others 其他可选功能点
     * @return 功能点容器对象
     */
    Functions add(FunctionPoint point, FunctionPoint... others);

    /**
     * 移除功能点
     *
     * @param points 功能点集合
     * @return 功能点容器对象
     */
    Functions remove(Collection<FunctionPoint> points);

    /**
     * 删除功能点
     *
     * @param point  功能点
     * @param others 其他可选功能点
     * @return 功能点容器对象
     */
    Functions remove(FunctionPoint point, FunctionPoint... others);

    /**
     * 功能点构建类
     *
     * @return Builder
     */
    static Builder builder() {
        return new Builder();
    }

    final class Builder {
        private BigInteger functions;
        private Set<FunctionPoint> functionPoints;

        public Builder of(BigInteger bigInteger) {
            this.functions = bigInteger;
            return this;
        }

        public Builder of(String value) {
            this.functions = new BigInteger(value);
            return this;
        }

        public Builder of(long longValue) {
            this.functions = BigInteger.valueOf(longValue);
            return this;
        }

        public Builder of(int intValue) {
            return of((long) intValue);
        }

        public Builder functions(Collection<? extends FunctionPoint> points) {
            if (functionPoints == null) {
                functionPoints = new HashSet<>();
            }
            functionPoints.addAll(points);
            return this;
        }

        public Builder functions(FunctionPoint[] points) {
            return functions(Arrays.asList(points));
        }

        public Builder function(FunctionPoint point) {
            return functions(Collections.singletonList(point));
        }

        public Builder functions(FunctionPoint point, FunctionPoint... others) {
            List<FunctionPoint> points = new ArrayList<>();
            points.add(point);
            points.addAll(Arrays.asList(others));
            return functions(points);
        }

        public Functions build() {
            BigInteger result = functions;
            if (functions == null) result = BigInteger.ZERO;
            if (functionPoints != null && !functionPoints.isEmpty()) {
                result = functionPoints.stream()
                        /* functions 中不包含的功能点才累加 */
                        .filter(point -> functions == null || !point.get().or(functions).equals(functions))
                        .reduce(result, (pre, cur) -> pre.add(cur.get()), BigInteger::add);
            }
            return new DefaultFunctions(result);
        }

    }

}
