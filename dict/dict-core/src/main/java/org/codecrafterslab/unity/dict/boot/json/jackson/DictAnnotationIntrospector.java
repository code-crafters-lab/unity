package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.SerializeHolder;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

@Slf4j
public class DictAnnotationIntrospector extends AnnotationIntrospector {

    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }

    @Override
    protected <A extends Annotation> A _findAnnotation(Annotated ann, Class<A> annoClass) {
        A result = super._findAnnotation(ann, annoClass);
        if (ann.hasAnnotation(annoClass) && DictSerialize.class.isAssignableFrom(annoClass)) {
            result = AnnotationUtils.synthesizeAnnotation(result, annoClass);
        }
        return result;
    }


    @Override
    public Object findSerializer(Annotated annotated) {
        if (annotated instanceof AnnotatedClass) {
            Class<?> aClass = ((AnnotatedClass) annotated).getAnnotated();
            if (DictionaryItem.class.isAssignableFrom(aClass)) {
                DictSerialize annotation = annotated.getAnnotation(DictSerialize.class);
                log.debug("{}", annotated);
            }
        }
        if (annotated instanceof AnnotatedMethod) {
            String name = annotated.getName();
            Pattern compile = Pattern.compile("service", Pattern.CASE_INSENSITIVE);
            JavaType type1 = annotated.getType();
            if (compile.matcher(name).find()) {
                AnnotatedMethod annotatedMethod = (AnnotatedMethod) annotated;
                // 类上注解
                DictSerialize d1 = AnnotationUtils.findAnnotation(annotatedMethod.getDeclaringClass(),
                        DictSerialize.class);
                DictSerialize d2 = _findAnnotation(annotated, DictSerialize.class);

                SerializeHolder serializeHolder = SerializeHolder.of(d1, d2);
                log.debug("{}", serializeHolder);
            }

        }
        return super.findSerializer(annotated);
    }

    @Override
    public Object findDeserializer(Annotated annotated) {
        if (annotated instanceof AnnotatedClass && EnumDictItem.class.isAssignableFrom(((AnnotatedClass) annotated).getAnnotated())) {
            AnnotatedClass annotatedClass = (AnnotatedClass) annotated;
            return findDeserializer(annotatedClass);
        }

        if (annotated instanceof AnnotatedMethod) {
            String name = annotated.getName();
            Pattern compile = Pattern.compile("service", Pattern.CASE_INSENSITIVE);
            if (compile.matcher(name).find()) {
                AnnotatedMethod annotatedMethod = (AnnotatedMethod) annotated;
                // 类
                Class<?> declaringClass = annotatedMethod.getDeclaringClass();
                JavaType parameterType = annotatedMethod.getParameterType(0);

                JavaType contentType = parameterType.getContentType();
                /* contentType 说明是容器类型，具体可能是 ArrayType,CollectionLikeType,MapLikeType,ReferenceType */
                if (contentType != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("容器类型 => {} ,实际类型 => {} ", parameterType.getRawClass(), contentType.getRawClass());
                    }
                }

            }
        }

        if (annotated instanceof AnnotatedField) {
            log.error("{} => {}", annotated.getName(), ((AnnotatedField) annotated).getMember());
        }
        return super.findDeserializer(annotated);
    }

    private Object findDeserializer(AnnotatedClass annotatedClass) {
        Class<?> aClass = annotatedClass.getAnnotated();
        log.debug("找到了枚举实现类 => {}", aClass);
        return null;
    }
}
