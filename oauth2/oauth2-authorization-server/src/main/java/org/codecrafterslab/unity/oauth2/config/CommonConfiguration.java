package org.codecrafterslab.unity.oauth2.config;

import org.codecrafterslab.unity.oauth2.authentication.ThirdCodeAuthenticationToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;

@Configuration
public class CommonConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient webClient = RegisteredClient.withId("0").clientId("web")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(ThirdCodeAuthenticationToken.THIRD_AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // 公共客户端不会产生刷新令牌
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .redirectUri("http://localhost:5000/auth/callback")
                .redirectUri("http://localhost:5000/api/auth/callback/jqsoft")
                .redirectUri("http://localhost:5000")
                .redirectUri("http://127.0.0.1:5000")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.PHONE)
                .scope(OidcScopes.EMAIL)
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true).requireAuthorizationConsent(true)
                        .build()).build();

        RegisteredClient dingtalk = RegisteredClient.withId("1")
                .clientId("dingopfniakkw72klkjv") // 如果是第三方OAuth2客户端，这里必须与第三方客户端ID保持一致
//                .clientSecret("{noop}6Il0DuPZPPIr-OG03uMrnqDNu_o03tpIkK03ScpuEPP6NAw7J52D0LWPvTjRf4BR")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(ThirdCodeAuthenticationToken.THIRD_AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .redirectUri("http://127.0.0.1:9000/login/oauth2/code/dingtalk")
//                .redirectUri("http://localhost:5000")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.PHONE)
                .scope(OidcScopes.EMAIL)
                .scope("corpid")
                .clientSettings(
                        ClientSettings.builder()
                                // 是否需要用户确认一下客户端需要获取用户的哪些权限
                                // 比如：客户端需要获取用户的 用户信息、用户照片 但是此处用户可以控制只给客户端授权获取 用户信息。
                                .requireAuthorizationConsent(true)
                                .setting("settings.client.provider", "dingtalk")
                                .build()
                )
                .tokenSettings(
                        TokenSettings.builder()
                                // accessToken 的有效期
                                .accessTokenTimeToLive(Duration.ofHours(2))
                                // 访问令牌格式
                                .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                                // refreshToken 的有效期
                                .refreshTokenTimeToLive(Duration.ofDays(7))
                                // 是否可重用刷新令牌
                                .reuseRefreshTokens(false)
                                .build()
                )
                .build();

        return new InMemoryRegisteredClientRepository(webClient, dingtalk);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    OAuth2AuthorizationService authorizationService(RegisteredClientRepository registeredClientRepository) {
//        return new InMemoryOAuth2AuthorizationService();
//    }

//    @Bean
//    public JdbcOAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
//                                                               RegisteredClientRepository
//                                                                       registeredClientRepository) {
//        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
//    }
//
//    @Bean
//    public JdbcOAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
//                                                                             RegisteredClientRepository
//                                                                                     registeredClientRepository) {
//        // Will be used by the ConsentController
//        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
//    }
}
