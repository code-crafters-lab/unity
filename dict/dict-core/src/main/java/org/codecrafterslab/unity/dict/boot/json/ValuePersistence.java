package org.codecrafterslab.unity.dict.boot.json;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 字典项局部序列化注解
 *
 * @author Wu Yujie
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@Documented
public @interface ValuePersistence {

    @AliasFor("scopes")
    Mode value() default Mode.AUTO;

    /**
     * 序列号字段范围
     */
    enum Mode {
        AUTO,
        ACCUMULATION,
        COMMA_SPLIT,
    }

}
