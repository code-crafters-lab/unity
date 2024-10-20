package com.voc.security.autoconfigure;

import com.voc.security.ResourceServerProperties;
import com.voc.security.config.ResourceServerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2021/06/14 08:27
 */
@Configuration
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@ConditionalOnClass(DefaultAuthenticationEventPublisher.class)
@EnableConfigurationProperties({ResourceServerProperties.class})
@Import({ResourceServerConfiguration.class})
public class ResourceServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BearerTokenResolver.class)
    BearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
        bearerTokenResolver.setAllowFormEncodedBodyParameter(true);
        bearerTokenResolver.setAllowUriQueryParameter(true);
        return bearerTokenResolver;
    }

    //    @Bean
//    OpaqueTokenIntrospector opaqueTokenIntrospector() throws Exception {
//        return new NimbusOpaqueTokenIntrospector("http://127.0.0.1:9000/oauth2/introspect", new RestTemplate());
//    }

}
