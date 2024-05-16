package org.codecrafterslab.unity.dict.boot.handler;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * @author WuYujie
 * @since 1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnClass({TypeHandler.class, ConfigurationCustomizer.class})
public class MyBatisPlusTypeHandlerConfiguration {

    @Bean
    ConfigurationCustomizer mybatisPlusTypeHandler(Consumer<TypeHandlerRegistry> consumer) {
        return configuration -> consumer.accept(configuration.getTypeHandlerRegistry());
    }

}
