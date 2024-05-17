package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.introspect.Annotated;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.boot.json.annotation.DictDeserialize;
import org.codecrafterslab.unity.dict.boot.json.annotation.DictSerialize;

import java.lang.annotation.Annotation;

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
        DictSerialize dictSerialize = _findAnnotation(annotated, DictSerialize.class);
        if (dictSerialize != null) {
            if (log.isDebugEnabled()) {
                log.debug("find serializer for {}", annotated);
            }
            // todo 优化返回序列
            // return new DictionaryItemSerializer(null, null);
        }
        return super.findSerializer(annotated);
    }

    @Override
    public Object findDeserializer(Annotated annotated) {
//        AnnotatedMethod annotatedMethod = (AnnotatedMethod) annotated;
        // todo 怎么获取到 BeanProperty
        DictDeserialize dictDeserializer = _findAnnotation(annotated, DictDeserialize.class);
        if (dictDeserializer != null) {
            if (log.isDebugEnabled()) {
                log.debug("find deserializer for {}", annotated);
            }
//            return new DictionaryItemDeserializer();
        }
        return super.findDeserializer(annotated);
    }
}
