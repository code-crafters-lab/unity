package org.codecrafterslab.unity.dict.api.func;

import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.api.func.impl.DefaultFunctions;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 功能点描述（容器）
 *
 * @author WuYujie
 * @since 0.1.2
 */
public interface Functions extends Supplier<BigInteger> {

    /**
     * 功能点构建类
     *
     * @return Builder
     */
    static Builder builder() {
        return new Builder();
    }

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
     * @param point  功能点
     * @param points 其他可选功能点
     * @return 是否包含指定功能点
     */
    boolean has(FunctionPoint point, FunctionPoint... points);

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
     * @param points 其他可选功能点
     * @return 是否包含任何一项功能点
     */
    boolean hasAny(FunctionPoint point, FunctionPoint... points);

    /**
     * 任何一项功能都没有
     *
     * @param point  功能点
     * @param points 其他可选功能点
     * @return 是否包不包含所有的功能点
     */
    boolean hasNone(FunctionPoint point, FunctionPoint... points);

    Functions add(BigInteger bigInteger);

    Functions add(String value);

    Functions add(long longValue);

    Functions add(int intValue);

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

    Functions remove(BigInteger bigInteger);

    Functions remove(String value);

    Functions remove(long longValue);

    Functions remove(int intValue);

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
     * @param points 其他可选功能点
     * @return 功能点容器对象
     */
    Functions remove(FunctionPoint point, FunctionPoint... points);

    <T extends FuncEnumDictItem> Collection<T> resolveFuncEnum(Class<T> clazz);

    final class Builder {
        private final Set<BigInteger> functions;
        private final Set<FunctionPoint> functionPoints;

        public Builder() {
            functions = new LinkedHashSet<>();
            functionPoints = new LinkedHashSet<>();
        }

        public Builder of(BigInteger bigInteger) {
            this.functions.add(bigInteger);
            return this;
        }

        public Builder of(String value) {
            return of(new BigInteger(value));
        }

        public Builder ofHex(String hex) {
            return of(new BigInteger(hex, 16));
        }

        public Builder of(long longValue) {
            return of(BigInteger.valueOf(longValue));
        }

        public Builder of(int intValue) {
            return of((long) intValue);
        }

        public Builder of(FunctionPoint point, FunctionPoint... points) {
            Stream.concat(Stream.of(point), Arrays.stream(points)).parallel().forEach(functionPoints::add);
            return this;
        }

        public Builder functions(Collection<? extends FunctionPoint> points) {
            functionPoints.addAll(points);
            return this;
        }

        public Builder functions(FunctionPoint[] points) {
            return functions(Arrays.asList(points));
        }

        public Builder function(FunctionPoint point, FunctionPoint... points) {
            return of(point, points);
        }

        public Functions build() {
            BigInteger result = Stream
                    .concat(functions.stream(), functionPoints.stream().map(FunctionPoint::get))
                    .parallel().reduce(BigInteger.ZERO, BigInteger::or);
            return new DefaultFunctions(result);
        }

    }

}
