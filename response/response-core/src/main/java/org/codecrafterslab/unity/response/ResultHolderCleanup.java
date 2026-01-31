package org.codecrafterslab.unity.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

/**
 * 清理 {@link ResultHolder#clear()} 数据
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2026/01/28 15:00
 */
@Slf4j
@RestControllerAdvice
public class ResultHolderCleanup implements ResponseBodyAdvice<Object>, Ordered {

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter methodParameter,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        Optional.ofNullable(ResultHolder.getData()).ifPresent(data -> {
            if (log.isDebugEnabled()) {
                log.debug("Clean ResultHolder Data: {}", data);
            }
            ResultHolder.clear();
        });
        return body;
    }

}
