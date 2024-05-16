package org.codecrafterslab.unity.dict.boot.json.annotation;

import java.lang.annotation.*;

/**
 * 字典项局部反序列注解
 *
 * @author Wu Yujie
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictDeserialize {

    /**
     * 反序列号根据字段
     *
     * @return
     */
    By value() default By.CODE;

    /**
     * 忽略大小写
     *
     * @return boolean
     */
    boolean caseInsensitive() default false;

    enum By {
        AUTO,
        CODE,
        VALUE,
        LABEL
    }
}
