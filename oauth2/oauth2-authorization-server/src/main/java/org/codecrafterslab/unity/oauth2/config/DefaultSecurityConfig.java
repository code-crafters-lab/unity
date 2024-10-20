package org.codecrafterslab.unity.oauth2.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author Joe Grandja
 * @author Steve Riesenberg
 * @since 1.1
 */
@EnableWebSecurity
@Configuration
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class DefaultSecurityConfig {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 5)
    SecurityFilterChain defaultSecurityFilterChain2(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/assets/**", "/actuator/health/liveness").permitAll().anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.oauth2Login(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

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

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user1 =
                User.withUsername("admin").password("123456").passwordEncoder(passwordEncoder::encode).roles("USER").build();
        UserDetails user2 =
                User.withUsername("demo").password("123456").passwordEncoder(passwordEncoder::encode).roles("USER").build();
        return new InMemoryUserDetailsManager(user1, user2);
    }

//    @Bean
//    public SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }
//
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }

}
