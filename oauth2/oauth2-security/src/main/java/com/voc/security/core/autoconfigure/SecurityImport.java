package com.voc.security.core.autoconfigure;

import com.voc.security.core.autoconfigure.config.IgnoreWebSecurityCustomizer;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2021/06/14 08:25
 */
public class SecurityImport implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return Arrays.stream(classes()).map(Class::getCanonicalName)
                .collect(Collectors.toList()).toArray(new String[]{});
    }

    Class<?>[] classes() {
        return (Class<?>[]) new Class[]{
                IgnoreWebSecurityCustomizer.class
//                AnnotationListener.class,
//                TokenController.class,
        };
    }
}
