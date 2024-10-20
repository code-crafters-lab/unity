package org.codecrafterslab.unity.oauth2.config;

import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import org.codecrafterslab.unity.oauth2.federation.FederatedIdentityIdTokenCustomizer;
import org.codecrafterslab.unity.oauth2.jose.Jwks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      RegisteredClientRepository registeredClientRepository) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        OAuth2AuthorizationServerConfigurer ass = http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);
//        ass.registeredClientRepository(registeredClientRepository);
        ass.authorizationServerMetadataEndpoint(metadataEndpoint -> {
            metadataEndpoint.authorizationServerMetadataCustomizer(builder -> {
                builder.grantType(AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
                builder.grantType(AuthorizationGrantType.REFRESH_TOKEN.getValue());
                builder.grantType(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());
                // oauth2 授权类型支持密码模式
                builder.grantType(AuthorizationGrantType.PASSWORD.getValue());
                builder.grantType(AuthorizationGrantType.DEVICE_CODE.getValue());
                builder.grantType(AuthorizationGrantType.TOKEN_EXCHANGE.getValue());

                builder.tokenRevocationEndpointAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                        .getValue());
                builder.tokenRevocationEndpointAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST
                        .getValue());
                builder.tokenRevocationEndpointAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT
                        .getValue());
                builder.tokenRevocationEndpointAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT
                        .getValue());
                builder.tokenRevocationEndpointAuthenticationMethod(ClientAuthenticationMethod.NONE.getValue());
                builder.tokenRevocationEndpointAuthenticationMethod(ClientAuthenticationMethod.TLS_CLIENT_AUTH
                        .getValue());
                builder.tokenRevocationEndpointAuthenticationMethod(ClientAuthenticationMethod
                        .SELF_SIGNED_TLS_CLIENT_AUTH.getValue());

                builder.codeChallengeMethod(CodeChallengeMethod.PLAIN.getValue());
                builder.codeChallengeMethod(CodeChallengeMethod.S256.getValue());

                builder.scope("openid");
                builder.scope("profile");
                builder.scope("email");

                builder.build();
            });
        });
        // Enable OpenID Connect 1.0
        ass.oidc(Customizer.withDefaults());

        // 跨域配置
        http.cors(Customizer.withDefaults());

        // Redirect to the login page when not authenticated from the authorization endpoint
//        http.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(new
//        LoginUrlAuthenticationEntryPoint("/login"), new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

        // Accept access tokens for User Info and/or Client Registration
        http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }


//    @Bean
//    public JdbcOAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
//                                                               RegisteredClientRepository
//                                                               registeredClientRepository) {
//        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
//    }
//
//    @Bean
//    public JdbcOAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
//                                                                             RegisteredClientRepository
//                                                                             registeredClientRepository) {
//        // Will be used by the ConsentController
//        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
//    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer() {
        return new FederatedIdentityIdTokenCustomizer();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.setAllowCredentials(false);
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        ECKey ecKey = Jwks.generateEc();
        OctetSequenceKey octetSequenceKey = Jwks.generateSecret();
        JWKSet jwkSet = new JWKSet(Arrays.asList(rsaKey, ecKey, octetSequenceKey));
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

//    @Bean
//    public AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder().build();
//    }

}
