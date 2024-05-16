package org.codecrafterslab.unity.dict.boot.json.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 字典项局部序列化注解
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/09 13:54
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@Documented
public @interface DictSerialize {

    @AliasFor("scopes")
    Scope[] value() default {};

    @AliasFor("value")
    Scope[] scopes() default {};

    /**
     * 唯一标识字段名称
     *
     * @return String
     */
    String id() default "";

    /**
     * 编码标识字段名称
     *
     * @return String
     */
    String code() default "";

    /**
     * 实际值字段名称
     *
     * @return String
     * @since 1.1.0
     */
    @Deprecated
    String val() default "";

    /**
     * 显示值字段名称
     *
     * @return String
     */
    String label() default "";

    /**
     * 描述字段名称
     *
     * @return String
     */
    String description() default "";

    /**
     * 序列号字段范围
     */
    enum Scope {
        ALL,
        ID,
        CODE,
        VALUE,
        LABEL,
        SORT,
        DISABLED,
        DESCRIPTION
    }

}
