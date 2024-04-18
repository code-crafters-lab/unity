package org.codecrafterslab.unity.dict.api;

import org.codecrafterslab.unity.exception.core.BizException;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 枚举数据字典
 *
 * @author Wu Yujie
 */
public interface EnumDictItem<V> extends DictionaryItem<V> {

    Logger log = LoggerFactory.getLogger(EnumDictItem.class);

    /**
     * {@link Enum#ordinal()}
     *
     * @return 枚举序号, 如果枚举顺序改变, 此值将被变动
     */
    int ordinal();

    /**
     * the name of this enum constant
     * {@link Enum#name()}
     *
     * @return 枚举名称
     */
    String name();

    @Override
    default Integer getSort() {
        return ordinal();
    }

    /**
     * 枚举选项的描述,对一个选项进行详细的描述有时候是必要的.默认值为 {@code null}
     *
     * @return 描述
     */
    @Override
    default String getDescription() {
        return null;
    }

    /**
     * 枚举选项的编码,默认值为枚举名称的小写形式
     *
     * @return 描述
     */
    @Override
    default String getCode() {
        return name().toLowerCase();
    }

    /**
     * 对比是否和 value相等,对比地址,值,value转为string忽略大小写对比,text忽略大小写对比
     * 与枚举名称是否相等（忽略大小写）
     *
     * @param value 值
     * @return 是否相等
     * @since 0.2.0
     */
    @Deprecated
    default boolean eq(Object value) {
        return this == value || name().equalsIgnoreCase(String.valueOf(value)) || getValue().equals(value);
    }

    /**
     * 从指定的枚举类中查找想要的枚举,并返回一个{@link List},如果未找到,则返回一个{@link Collections#emptyList()}
     *
     * @param type      实现了{@link EnumDictItem}的枚举类
     * @param predicate 判断逻辑
     * @param <T>       枚举类型
     * @return 查找到的结果
     */
    static <T extends EnumDictItem<?>> List<T> findByCondition(Class<T> type, Predicate<T> predicate) {
        if (type.isEnum()) {
            return Arrays.stream(type.getEnumConstants()).filter(predicate).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 查找所有枚举值
     *
     * @param type 实现了{@link EnumDictItem}的枚举类
     * @param <T>  枚举类型
     * @return List<T>
     */
    static <T extends EnumDictItem<?>> List<T> findAll(Class<T> type) {
        return findByCondition(type, item -> true);
    }

    /**
     * 根据枚举的{@link EnumDictItem#getValue()}来查找.
     *
     * @param type  Class<T>
     * @param value BigInteger
     * @param <T>   枚举类型
     * @return 查找到的结果
     * @see #findByCondition(Class, Predicate)
     */
    static <T extends EnumDictItem<?>> T findByValue(Class<T> type, Object value) {
        return findByCondition(type, item -> item.getValue().equals(value)).stream().findFirst().orElse(null);
    }

    /**
     * 根据枚举的{@link EnumDictItem#name()}来查找.
     *
     * @param type Class<T>
     * @param code String
     * @param <T>  枚举类型
     * @return 查找到的结果
     * @see #findByCondition(Class, Predicate)
     */
    static <T extends EnumDictItem<?>> T findByCode(Class<T> type, String code) {
        return findByCondition(type, item -> item.name().equalsIgnoreCase(code)).stream().findFirst().orElse(null);
    }

    /**
     * @param type Class<T>
     * @param <T>  枚举类型
     * @return 业务异常
     */
    static <T extends EnumDictItem<?>> BizException unsupported(Class<T> type, Object arg) {
        if (log.isWarnEnabled()) {
            String values = findAll(type).stream()
                    .map(d -> d.getCode() + "/" + d.getValue().toString())
                    .collect(Collectors.joining(","));
            log.warn("不受支持的值 {} in [{}]", arg, values);
        }
        return new BizException(BizStatus.UN_SUPPORTED_VALUE);
    }

    /**
     * 根据枚举的{@link EnumDictItem#name()}来查找.
     *
     * @param type      Class<T>
     * @param parameter 字典项实际值或编码
     * @param <T>       枚举类型
     * @return 查找到的结果
     * @see #findByCondition(Class, Predicate)
     */
    static <T extends EnumDictItem<?>> T find(Class<T> type, Object parameter) {
        T result;
        if (parameter instanceof String) {
            result = findByCode(type, (String) parameter);
            if (!Objects.isNull(result)) return result;
        }
        result = findByValue(type, parameter);
        if (Objects.isNull(result)) throw unsupported(type, parameter);
        return result;
    }

}
