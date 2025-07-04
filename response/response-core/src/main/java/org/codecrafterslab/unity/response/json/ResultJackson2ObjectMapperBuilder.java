package org.codecrafterslab.unity.response.json;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2020/09/27 09:07
 */
public class ResultJackson2ObjectMapperBuilder implements Jackson2ObjectMapperBuilderCustomizer, Ordered {

    private final ResultSerializer resultSerializer;

    public ResultJackson2ObjectMapperBuilder(ResultSerializer resultSerializer) {
        this.resultSerializer = resultSerializer;
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        ResultModule resultModule = new ResultModule(resultSerializer);
        builder.modules(resultModule);
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
