package org.codecrafterslab.unity.dict.boot.json.jackson;

import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.Features;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Wu Yujie
 * @since 1.0.0
 */
public class DictJackson2ObjectMapperBuilder implements Jackson2ObjectMapperBuilderCustomizer, Ordered {
    private final ApplicationContext applicationContext;
    private final DictProperties dictProperties;

    public DictJackson2ObjectMapperBuilder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.dictProperties = applicationContext.getBean(DictProperties.class);
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        Boolean annoIntrospector = dictProperties.getFeatures().getOrDefault(Features.ANNOTATION_INTROSPECTOR, false);
        if (annoIntrospector) {
            builder.annotationIntrospector(new JacksonAnnotationIntrospector());
            builder.annotationIntrospector(primary -> AnnotationIntrospectorPair.pair(primary,
                    new DictAnnotationIntrospector()));
        }

        DictModule dictModule = new DictModule(applicationContext);
        builder.modulesToInstall(dictModule);
    }

    @Override
    public int getOrder() {
        return 1;
    }


}
