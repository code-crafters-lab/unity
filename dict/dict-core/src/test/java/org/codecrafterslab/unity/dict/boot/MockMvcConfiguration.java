package org.codecrafterslab.unity.dict.boot;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;

import java.nio.charset.StandardCharsets;

/**
 * @author Wu Yujie
 */
@Configuration
public class MockMvcConfiguration implements MockMvcBuilderCustomizer {

    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
        builder.defaultResponseCharacterEncoding(StandardCharsets.UTF_8);
    }

}
