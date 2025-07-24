package org.codecrafterslab.unity.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.response.annotation.ResponseResult;
import org.codecrafterslab.unity.response.api.Result;
import org.codecrafterslab.unity.response.properties.ResponseWrapperProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 接口响应数据统一包装为 {@link Result}
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2020/11/18 18:53
 */
@Slf4j
@RestControllerAdvice
public class ResultAdvice implements ResponseBodyAdvice<Object> {
    private final Set<String> ignoredClassName = new HashSet<>();
    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    private final Class<? extends Annotation>[] annotations = new Class[]{
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            DeleteMapping.class,
            PutMapping.class,
            PatchMapping.class
    };

    public ResultAdvice(ResponseWrapperProperties wrapperProperties, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        Set<String> ignoredClass = wrapperProperties.getIgnoredClass();
        if (ignoredClass != null) {
            ignoredClassName.addAll(ignoredClass);
        }
    }

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return !this.ignored(methodParameter) && this.supports(methodParameter);
    }

    /**
     * 忽略的类型（方法所在类或方法返回值）
     *
     * @param methodParameter MethodParameter
     * @return boolean
     */
    private boolean ignored(MethodParameter methodParameter) {
        Set<String> classes = Stream.of(methodParameter.getDeclaringClass(), methodParameter.getParameterType())
                .map(Class::getCanonicalName).collect(Collectors.toSet());
        return ignoredClassName.stream().anyMatch(classes::contains);
    }

    protected boolean unSupportBody(Object body) {
        return body == null || Result.class.isAssignableFrom(body.getClass()) || ignoredClassName.contains(body.getClass().getCanonicalName());
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter methodParameter,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (!unSupportBody(body)) return body;
        Object out = ResultUtils.success(body);
        /* 如果是 StringHttpMessageConverter，说明返回的数据是字符，用 objectMapper 序列化后返回 */
        if (selectedConverterType.isAssignableFrom(StringHttpMessageConverter.class)) {
            try {
                out = objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }
        return out;
    }

    private boolean supports(MethodParameter methodParameter) {
        Method method = methodParameter.getMethod();
        assert method != null;
        AnnotatedElement element = methodParameter.getAnnotatedElement();
        /* 前置条件，必须是 annotations 中指定注解的方法 */
        boolean preCondition = Arrays.stream(annotations).filter(Class::isAnnotation).anyMatch(element::isAnnotationPresent);
        return preCondition && this.methodWrapper(method);
    }

    /**
     * 类上指定方法是否包装响应结果
     *
     * @return boolean
     */
    private boolean methodWrapper(Method method) {
        /* 1. 方法上的注解优先 */
        ResponseResult methodAnnotation = AnnotationUtils.getAnnotation(method, ResponseResult.class);
        if (methodAnnotation != null) {
            if (log.isDebugEnabled()) {
                log.debug("方法上注解 value：{} wrapped：{}", methodAnnotation.value(), methodAnnotation.wrapped());
            }
            return methodAnnotation.value();
        }

        /* 2. 方法上不存在注解时使用类上注解 */
        Class<?> clazz = method.getDeclaringClass();
        ResponseResult classAnnotation = AnnotationUtils.getAnnotation(clazz, ResponseResult.class);
        if (classAnnotation != null) {
            if (log.isDebugEnabled()) {
                log.debug("类上注解 value：{} wrapped：{}", classAnnotation.value(), classAnnotation.wrapped());
            }
            return classAnnotation.value();
        }

        /* 3. 都不存在时返回 true */
        return true;
    }
}
