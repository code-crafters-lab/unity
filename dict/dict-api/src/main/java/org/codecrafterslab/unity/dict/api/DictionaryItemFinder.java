package org.codecrafterslab.unity.dict.api;

import org.codecrafterslab.unity.exception.core.BizException;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DictionaryItemFinder {
    Logger log = LoggerFactory.getLogger(DictionaryItemFinder.class);

    <T extends DictionaryItem<?>> Predicate<Class<T>> typePredicate();

    <T extends DictionaryItem<?>> List<T> findByCondition(Class<T> type, Predicate<Class<T>> typePredicate, Predicate<T> predicate);

    default <T extends DictionaryItem<?>> List<T> findByCondition(Class<T> type, Predicate<T> predicate) {
        return findByCondition(type, typePredicate(), predicate);
    }

    /**
     * 查找所有字典项
     *
     * @param type 实现了{@link DictionaryItem}的类
     * @param <T>  字典项类型
     * @return List<T>
     */
    default <T extends DictionaryItem<?>> List<T> findAll(Class<T> type) {
        return findByCondition(type, item -> true);
    }

    /**
     * 根据编码{@link DictionaryItem#getCode()}查找字典项
     *
     * @param type 字典项类型
     * @param code 字典项编码
     * @param <T>  字典项类型
     * @return 查找到的结果
     */
    default <T extends DictionaryItem<?>> T findByCode(Class<T> type, @Nullable String code) {
        if (Objects.isNull(code)) return null;
        return findByCondition(type, item -> item.getCode().equalsIgnoreCase(code.trim()))
                .stream().findFirst().orElse(null);
    }

    /**
     * 获取实际值的泛型类型
     *
     * @param type 实现了{@link  DictionaryItem}的类
     * @param <T>  字典项类型
     * @return Class<?>
     * @since 0.2.0
     */
    default <T extends DictionaryItem<?>> Class<?> getValueType(Class<T> type) {
        // TODO: 缓存实际值类型
        ResolvableType resolvableType = ResolvableType.forClass(type).as(DictionaryItem.class);
        if (ResolvableType.NONE.equals(resolvableType)) {
            throw new IllegalArgumentException("Cannot determine EnumDictItem's generic type for class " + type.getName());
        }
        return resolvableType.getGeneric().resolve();
    }

    /**
     * 根据实际值查找字典项
     *
     * @param type  字典项类型
     * @param value 字典项实际值
     * @param <T>   字典项类型
     * @return 查找到的结果
     */
    default <T extends DictionaryItem<?>> T findByValue(Class<T> type, @Nullable Object value) {
        if (Objects.isNull(value)) return null;
        return findByCondition(type, item -> {
                    if (value.getClass().equals(getValueType(type))) {
                        return item.getValue().equals(value);
                    } else {
                        return item.getValue().toString().equals(value.toString().trim());
                    }
                }
        ).stream().findFirst().orElse(null);
    }

    /**
     * 根据显示值{@link DictionaryItem#getLabel()} 查找字典项
     *
     * @param type  字典项类型
     * @param label 字典项显示值
     * @param <T>   字典项类型
     * @return 查找到的结果
     */
    default <T extends DictionaryItem<?>> T findByLabel(Class<T> type, @Nullable String label) {
        if (Objects.isNull(label)) return null;
        return findByCondition(type, item -> item.getLabel().equalsIgnoreCase(label.trim()))
                .stream().findFirst().orElse(null);
    }

    /**
     * 根据编码、实际值、显示值依次查找字典项
     *
     * @param type      字典项类型
     * @param parameter 字典项编码、实际值或显示值
     * @param <T>       字典项类型
     * @return 查找到的结果
     */
    default <T extends DictionaryItem<?>> T find(Class<T> type, @Nullable Object parameter) {
        if (Objects.isNull(parameter)) return null;
        T result;
        /* 1. 如果值是字符串先根据编码进行查找 */
        if (parameter instanceof String) {
            result = findByCode(type, (String) parameter);
            if (!Objects.isNull(result)) return result;
        }
        /* 2. 未找到到再根据实际值进行查找 */
        Object converted = valueConverter(type, parameter);
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
     * 实际值转换器
     *
     * @param type      字典项类型
     * @param parameter 字典项编码、实际值或显示值
     * @param <T>       字典项类型
     * @return 转换后的值
     */
    default <T extends DictionaryItem<?>> Object valueConverter(Class<T> type, Object parameter) {
        return parameter;
    }

    /**
     * 不受支持值的抛出异常
     *
     * @param type      字典项类型
     * @param parameter 字典项编码、实际值或显示值
     * @param <T>       字典项类型
     * @return 业务异常
     */
    default <T extends DictionaryItem<?>> BizException unsupported(Class<T> type, Object parameter) {
        if (log.isWarnEnabled()) {
            String values = findAll(type).stream()
                    .map(item -> String.join("/", Stream.of(item.getCode(), item.getValue().toString(), item.getLabel())
                            .filter(StringUtils::hasLength).collect(Collectors.toSet()))
                    ).collect(Collectors.joining(","));
            log.warn("{} => {} 不支持的值, 可用值范围为：[{}]", parameter, type.getName(), values);
        }
        return new BizException(BizStatus.UN_SUPPORTED_VALUE);
    }
}
