package org.codecrafterslab.unity.dict.boot.annotation;

import org.codecrafterslab.unity.dict.boot.json.jackson.ser.SerializeScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典项局部序列化注解
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/09 13:54
 */
@Target({ElementType.TYPE, ElementType.PACKAGE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DictSerialize {

    SerializeScope[] value() default {};

    /**
     * @return String
     */
    String scopes() default "";

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
     * 显示值字段名称
     *
     * @return String
     */
    String label() default "";

    /**
     * 排序字段名称
     *
     * @return String
     */
    String sort() default "";

    /**
     * 禁用状态字段名称
     *
     * @return String
     */
    String disabled() default "";

    /**
     * 描述字段名称
     *
     * @return String
     */
    String description() default "";

}
