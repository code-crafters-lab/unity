package org.codecrafterslab.unity.dict.api;

import org.codecrafterslab.unity.exception.core.BizException;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 枚举数据字典项
 *
 * @author Wu Yujie
 */
public interface EnumDictItem<V> extends DictionaryItem<V> {

    Logger log = LoggerFactory.getLogger(EnumDictItem.class);

    static <T extends EnumDictItem<?>> List<T> findByCondition(Class<T> type, Predicate<Class<T>> typePredicate,
                                                               Predicate<T> predicate) {
        if (type.isEnum() && typePredicate.test(type)) {
            return Arrays.stream(type.getEnumConstants()).filter(predicate).collect(Collectors.toList());
        }
        return Collections.emptyList();
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
        return findByCondition(type, EnumDictItem.class::isAssignableFrom, predicate);
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
     * 获取值的泛型类型
     *
     * @param type 实现了{@link EnumDictItem}的枚举类
     * @param <T>  枚举类型
     * @return Class<?>
     * @since 0.2.0
     */
    static <T extends EnumDictItem<?>> Class<?> getValueType(Class<T> type) {
        ResolvableType resolvableType = ResolvableType.forClass(type).as(EnumDictItem.class);
        if (ResolvableType.NONE.equals(resolvableType)) {
            throw new IllegalArgumentException("Cannot determine EnumDictItem's generic type for class " + type.getName());
        }
        return resolvableType.getGeneric().resolve();
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
    static <T extends EnumDictItem<?>> T findByValue(Class<T> type, @Nullable Object value) {
        if (Objects.isNull(value)) return null;
        return findByCondition(type, item -> value.getClass().equals(getValueType(type)) ?
                item.getValue().equals(value) : item.getValue().toString().equals(value.toString().trim())).stream().findFirst().orElse(null);
    }

    /**
     * 根据枚举的{@link EnumDictItem#getCode()}来查找.
     *
     * @param type Class<T>
     * @param code String
     * @param <T>  枚举类型
     * @return 查找到的结果
     * @see #findByCondition(Class, Predicate)
     */
    static <T extends EnumDictItem<?>> T findByCode(Class<T> type, @Nullable String code) {
        if (Objects.isNull(code)) return null;
        return findByCondition(type, item -> item.getCode().equalsIgnoreCase(code.trim())).stream().findFirst().orElse(null);
    }

    /**
     * 根据枚举的{@link EnumDictItem#getLabel()} 来查找.
     *
     * @param type  Class<T>
     * @param label String
     * @param <T>   枚举类型
     * @return 查找到的结果
     * @see #findByCondition(Class, Predicate)
     */
    static <T extends EnumDictItem<?>> T findByLabel(Class<T> type, @Nullable String label) {
        if (Objects.isNull(label)) return null;
        return findByCondition(type, item -> item.getLabel().equalsIgnoreCase(label.trim())).stream().findFirst().orElse(null);
    }

    /**
     * @param type Class<T>
     * @param <T>  枚举类型
     * @return 业务异常
     */
    static <T extends EnumDictItem<?>> BizException unsupported(Class<T> type, Object arg) {
        if (log.isWarnEnabled()) {
            String values = findAll(type).stream()
                    .map(item -> String.join("/", Arrays.asList(item.getCode(), item.getValue().toString().trim(),
                            item.getLabel())))
                    .collect(Collectors.joining(","));
            log.warn("{} => {} 不支持的值, 可用值范围为：[{}]", arg, type.getName(), values);
        }
        return new BizException(BizStatus.UN_SUPPORTED_VALUE);
    }

    /**
     * 根据枚举的{@link EnumDictItem#name()}来查找.
     *
     * @param type      Class<T>
     * @param parameter 字典项编码、实际值或显示值
     * @param <T>       枚举类型
     * @return 查找到的结果
     * @see #findByCondition(Class, Predicate)
     */
    static <T extends EnumDictItem<?>> T find(Class<T> type, @Nullable Object parameter) {
        return find(type, parameter, val -> val);
    }

    /**
     * 根据枚举的{@link EnumDictItem#name()}来查找.
     *
     * @param type         Class<T>
     * @param parameter    字典项编码、实际值或显示值
     * @param valueConvert 实际值类型转换器
     * @param <T>          枚举类型
     * @return 查找到的结果
     * @see #findByCondition(Class, Predicate)
     */
    static <T extends EnumDictItem<?>> T find(Class<T> type, @Nullable Object parameter,
                                              Function<Object, Object> valueConvert) {
        if (Objects.isNull(parameter)) return null;

        T result;
        /* 1. 如果值是字符串先根据编码进行查找 */
        if (parameter instanceof String) {
            result = findByCode(type, (String) parameter);
            if (!Objects.isNull(result)) return result;
        }
        /* 2. 未找到到再根据实际值进行查找 */
        Object converted = valueConvert.apply(parameter);
        result = findByValue(type, converted);
        if (!Objects.isNull(result)) {
            return result;
        }
        /* 3. 实际值也未查到的情况且是字符，则根据显示值进行查找 */
        if (parameter instanceof String) {
            result = findByLabel(type, (String) parameter);
        }
        /* 4. 否则抛出异常 */
        if (Objects.isNull(result)) throw unsupported(type, parameter);
        return result;
    }

    /**
     * 枚举项的编码，默认值为枚举名称
     *
     * @return 描述
     */
    @Override
    default String getCode() {
        return name();
    }

    /**
     * @return 枚举序号, 如果枚举顺序改变, 此值将被变动
     * @see Enum#ordinal()
     */
    int ordinal();

    /**
     * the name of this enum constant
     *
     * @return 枚举名称
     * @see Enum#name()
     */
    String name();

    @Override
    default Integer getSort() {
        return ordinal();
    }

    /**
     * 枚举选项的描述，对每一个选项进行详细的描述有时候是必要的，默认值为 {@code null}
     *
     * @return 描述
     */
    @Override
    default String getDescription() {
        return null;
    }
}
