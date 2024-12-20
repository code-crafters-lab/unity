package com.voc.security.config;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/03/28 20:00
 */
@EnableWebSecurity
public class ResourceServerConfiguration {


//    @Bean
//    SecurityFilterChain restfulSecurityFilterChain(HttpSecurity http,
//                                                   ResultAuthenticationEntryPoint resultAuthenticationEntryPoint,
//                                                   ResultAccessDeniedHandler resultAccessDeniedHandler,
//                                                   RestfulAuthenticationFilter restfulAuthenticationFilter
//
//    ) throws Exception {
//        /* 1. 禁用 session */
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        /* 2. 异常处理 */
//        http.exceptionHandling(handling -> {
//            handling.authenticationEntryPoint(resultAuthenticationEntryPoint);
//            handling.accessDeniedHandler(resultAccessDeniedHandler);
//        });
//
//        http.apply(new BearerTokenAuthenticationFilterConfigurer<>());
//
//        /* oauth2 资源服务配置 */
//        http.oauth2ResourceServer(configurer -> {
//            configurer.authenticationEntryPoint(resultAuthenticationEntryPoint);
//            configurer.accessDeniedHandler(resultAccessDeniedHandler);
////            configurer.jwt();
//            configurer.opaqueToken();
//        });
//
//        /* 权限配置 */
////        http.authorizeRequests(
////                authorize -> {
////                    authorize
////                            .antMatchers(HttpMethod.OPTIONS).permitAll()
////                            .antMatchers("/messages/**").access("hasAuthority('SCOPE_message.read')")
////                            .antMatchers("/menus/**").access("hasAuthority('SCOPE_menu.read')")
////                            .anyRequest().authenticated();
////
////                }
////        );
//
//        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//            authorizationManagerRequestMatcherRegistry
//                    .antMatchers("/**").hasRole("")
//                    .anyRequest().authenticated();
////                    .anyRequest().access()
//        });
//
//
//        return http.build();
//    }

}
