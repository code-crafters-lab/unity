package org.codecrafterslab.unity.dict.boot.annotation;

import org.codecrafterslab.unity.dict.boot.combine.ScopesMode;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.SerializeScope;
import org.springframework.core.annotation.AliasFor;

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

    @AliasFor("scopes")
    SerializeScope[] value() default {};

    /**
     * 属性序列化范围
     *
     * @return SerializeScope[]
     */
    @AliasFor("value")
    SerializeScope[] scopes() default {};

    /**
     * 属性序列化范围输出模式
     *
     * @return {@link ScopesMode}
     */
    ScopesMode output() default ScopesMode.NO_OP;

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
