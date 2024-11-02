package org.codecrafterslab.unity.oauth2.config;

import org.codecrafterslab.unity.oauth2.client.dingtalk.DingTalkAccessTokenResponseConverter;
import org.codecrafterslab.unity.oauth2.client.dingtalk.DingTalkOAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.codecrafterslab.unity.oauth2.client.dingtalk.DingTalkOAuth2UserService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.DelegatingOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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
    OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient authorizationCodeTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();
        authorizationCodeTokenResponseClient.setRequestEntityConverter(new DingTalkOAuth2AuthorizationCodeGrantRequestEntityConverter());

        OAuth2AccessTokenResponseHttpMessageConverter accessTokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        accessTokenResponseHttpMessageConverter.setAccessTokenResponseConverter(new DingTalkAccessTokenResponseConverter());
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(),
                accessTokenResponseHttpMessageConverter, new MappingJackson2HttpMessageConverter()));
        authorizationCodeTokenResponseClient.setRestOperations(restTemplate);

        return authorizationCodeTokenResponseClient;
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 5)
    SecurityFilterChain defaultSecurityFilterChain2(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/assets/**", "/actuator/health/liveness").permitAll().anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.oauth2Login(oauth2 -> {
            /* 令牌端点配置 */
            oauth2.tokenEndpoint(tokenEndpoint -> {
                tokenEndpoint.accessTokenResponseClient(accessTokenResponseClient());
            });
            /* 用户信息端点配置 */
            oauth2.userInfoEndpoint(userInfoEndpoint -> {
                List<OAuth2UserService<OAuth2UserRequest, OAuth2User>> userServices =
                        Arrays.asList(new DingTalkOAuth2UserService(), new DefaultOAuth2UserService());
                userInfoEndpoint.userService(new DelegatingOAuth2UserService<>(userServices));
            });
        });
        http.cors(withDefaults());
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
