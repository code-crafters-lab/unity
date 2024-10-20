package com.voc.security.core.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/04/04 13:11
 */
@Configuration
@ConditionalOnClass(DefaultAuthenticationEventPublisher.class)
//@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class SecurityCoreAutoConfiguration {

    /**
     * 注入默认密码加密器
     *
     * @return PasswordEncoder
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    /**
//     * 注入系统根用户
//     *
//     * @param rootAccount 根用户配置
//     * @return AuthService
//     */
//    @Bean
//    AuthService rootAccountService(RootAccountProperties rootAccount) {
//        return new RootAccountService(rootAccount);
//    }

    /**
     * Spring Security 默认用户服务
     *
     * @return UserDetailsService
     */
//    @Bean
//    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails userDetails =
//                User.withUsername("admin").password("123456").passwordEncoder(passwordEncoder::encode).roles
//                ("USER").build();
//        return new InMemoryUserDetailsManager(userDetails);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder
//            passwordEncoder) {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//        /* // TODO: 2022/3/28 21:18 根据配置是否隐藏用户不存在异常 */
//        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
//        return daoAuthenticationProvider;
//    }
}
