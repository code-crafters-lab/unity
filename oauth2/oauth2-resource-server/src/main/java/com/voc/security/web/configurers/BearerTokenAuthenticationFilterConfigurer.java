package com.voc.security.web.configurers;

import com.voc.security.core.authentication.ResultAuthenticationEntryPoint;
import com.voc.security.core.authentication.ResultAuthenticationFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2020/10/10 11:03
 */
@Slf4j
public class BearerTokenAuthenticationFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractHttpConfigurer<BearerTokenAuthenticationFilterConfigurer<H>, H> {
    private ApplicationContext context;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    public void init(H http) throws Exception {
        context = http.getSharedObject(ApplicationContext.class);
        authenticationEntryPoint = context.getBean(ResultAuthenticationEntryPoint.class);
        authenticationFailureHandler = context.getBean(ResultAuthenticationFailureHandler.class);
    }

    @Override
    public void configure(H http) throws Exception {
        BearerTokenResolver bearerTokenResolver = getBearerTokenResolver();
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        BearerTokenAuthenticationFilter filter = new BearerTokenAuthenticationFilter(authenticationManager);
        filter.setBearerTokenResolver(bearerTokenResolver);
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter = postProcess(filter);
        http.addFilter(filter);
    }

    private BearerTokenResolver getBearerTokenResolver() {
        if (this.context.getBeanNamesForType(BearerTokenResolver.class).length == 1) {
            return this.context.getBean(BearerTokenResolver.class);
        } else {
            return new DefaultBearerTokenResolver();
        }
    }


}
