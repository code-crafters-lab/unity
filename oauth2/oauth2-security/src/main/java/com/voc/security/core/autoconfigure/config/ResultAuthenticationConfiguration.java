//package com.voc.security.core.autoconfigure.config;
//
//import com.voc.security.core.authentication.*;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//
/// **
// * @author Wu Yujie
// * @email coffee377@dingtalk.com
// * @time 2022/04/04 12:59
// */
//@Configuration
//public class ResultAuthenticationConfiguration {
//
//    /**
//     * 认证异常处理器
//     *
//     * @return AuthenticationEntryPoint
//     */
//    @Bean("resultAuthenticationEntryPoint")
//    @ConditionalOnMissingBean(ResultAuthenticationEntryPoint.class)
//    AuthenticationEntryPoint authenticationEntryPoint() {
//        return new ResultAuthenticationEntryPoint();
//    }
//
//    /**
//     * 无访问权限处理器
//     *
//     * @return AccessDeniedHandler
//     */
//    @Bean("resultAccessDeniedHandler")
//    @ConditionalOnMissingBean(ResultAccessDeniedHandler.class)
//    AccessDeniedHandler accessDeniedHandler() {
//        return new ResultAccessDeniedHandler();
//    }
//
//    /**
//     * 认证成功处理器
//     *
//     * @return AuthenticationSuccessHandler
//     */
//    @Bean("resultAuthenticationSuccessHandler")
//    @ConditionalOnMissingBean(ResultAuthenticationSuccessHandler.class)
//    AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new ResultAuthenticationSuccessHandler();
//    }
//
//    /**
//     * 认证失败处理器
//     *
//     * @return AuthenticationFailureHandler
//     */
//    @Bean("resultAuthenticationFailureHandler")
//    @ConditionalOnMissingBean(ResultAuthenticationFailureHandler.class)
//    AuthenticationFailureHandler authenticationFailureHandler() {
//        return new ResultAuthenticationFailureHandler();
//    }
//
//    /**
//     * 注销成功处理器
//     *
//     * @return LogoutSuccessHandler
//     */
//    @Bean("resultLogoutSuccessHandler")
//    @ConditionalOnMissingBean(ResultLogoutSuccessHandler.class)
//    LogoutSuccessHandler logoutSuccessHandler() {
//        return new ResultLogoutSuccessHandler();
//    }
//
//}
