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
import org.codecrafterslab.unity.dict.api.EnumDictItem;

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
//        log.warn("{} {}", annoClass, ann.getAnnotated().isAnnotationPresent(annoClass));
//        if (ann.getAnnotated() != null && ann.getAnnotated().isAnnotationPresent(annoClass)) {
//            final A a = AnnotatedElementUtils.findMergedAnnotation(ann.getAnnotated(), annoClass);
//            return a;
//        }
        return super._findAnnotation(ann, annoClass);
    }


    @Override
    public Object findSerializer(Annotated annotated) {
//        log.warn("{}", annotated);
//        Class<?> rawClass = annotated.getType().getRawClass();
//        if (EnumDictItem.class.isAssignableFrom(rawClass)) {
//            Class<?> rawType = annotated.getRawType();
//            log.warn("{} => {}", rawClass, rawType);
//            DictSerialize dictSerialize = _findAnnotation(annotated, DictSerialize.class);
//            if (dictSerialize != null) {
//                if (log.isDebugEnabled()) {
//                    log.debug("find serializer for {}", annotated);
//                }
//                // todo 优化返回序列
//                // return new DictionaryItemSerializer(null, null);
//            }
//        }
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
//        log.warn("找到了枚举实现类 => {}", aClass);
        //                String name = annotated.getName();
//                Class<?> aClass1 = annotated.getRawType();
//                JavaType type = annotated.getType();
//                log.error("{} => {} => {}", name, aClass1, type);
        return null;
    }
}
