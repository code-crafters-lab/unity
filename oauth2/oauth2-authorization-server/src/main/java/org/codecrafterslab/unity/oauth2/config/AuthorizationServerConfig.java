package org.codecrafterslab.unity.oauth2.config;

import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import org.codecrafterslab.unity.oauth2.authentication.ThirdClientAuthenticationConverter;
import org.codecrafterslab.unity.oauth2.authentication.ThirdClientAuthenticationProvider;
import org.codecrafterslab.unity.oauth2.authentication.ThirdCodeAuthenticationConverter;
import org.codecrafterslab.unity.oauth2.authentication.ThirdCodeAuthenticationProvider;
import org.codecrafterslab.unity.oauth2.customizer.OidcUserInfoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      RegisteredClientRepository registeredClientRepository) throws Exception {
        OAuth2AuthorizationServerConfigurer sas = new OAuth2AuthorizationServerConfigurer();
        RequestMatcher endpointsMatcher = sas.getEndpointsMatcher();

//        sas.getEndpointsMatcher()

//        OAuth2TokenEndpointConfigurer t = http.getConfigurer(OAuth2TokenEndpointConfigurer.class)
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//        OAuth2AuthorizationServerConfigurer sas = http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        sas.authorizationServerMetadataEndpoint(metadataEndpoint -> {
            metadataEndpoint.authorizationServerMetadataCustomizer(builder -> {
                // oauth2 授权类型支持密码模式
                builder.grantType(AuthorizationGrantType.PASSWORD.getValue());
                builder.codeChallengeMethod(CodeChallengeMethod.PLAIN.getValue());

                builder.scope(OidcScopes.PROFILE);
                builder.scope(OidcScopes.EMAIL);
                builder.scope(OidcScopes.PHONE);
                builder.scope("dingtalk");
                builder.scope("job_number");
            });
        });

        /* 授权服务器客户端认证 */
        sas.clientAuthentication(clientAuthentication -> {
            clientAuthentication.authenticationConverter(new ThirdClientAuthenticationConverter());
            clientAuthentication.authenticationProvider(new ThirdClientAuthenticationProvider(registeredClientRepository));
        });

        // Enable OpenID Connect 1.0
        sas.oidc(oidc -> {
            oidc.providerConfigurationEndpoint(Customizer.withDefaults());
            oidc.userInfoEndpoint(userInfo -> {
                userInfo.userInfoMapper(new OidcUserInfoMapper());
            });
        });


        sas.tokenEndpoint(tokenEndpoint -> {
            tokenEndpoint.accessTokenRequestConverter(new ThirdCodeAuthenticationConverter());
            tokenEndpoint.authenticationProvider(new ThirdCodeAuthenticationProvider(http, authorizationService));
        });

        http.securityMatcher(endpointsMatcher).authorizeHttpRequests((authorize) ->
                // todo 只有第三方临时授权码的请求才放行，目前全部放开
                authorize
//                        .requestMatchers("/oauth2/token").permitAll()
//                                authorize.requestMatchers(endpointsMatcher).permitAll()
                        .anyRequest().authenticated()).csrf((csrf) -> csrf.ignoringRequestMatchers(endpointsMatcher)).apply(sas);

        // 跨域配置
        http.cors(Customizer.withDefaults());

        // Redirect to the login page when not authenticated from the authorization endpoint
        http.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

        // Accept access tokens for User Info and/or Client Registration
        // http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()));

        // 第三方授权直接登录过滤器
//        ThirdCodeLoginAuthenticationFilter filter = new ThirdCodeLoginAuthenticationFilter("/oauth2/token");
//        http.addFilterBefore(filter, OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
