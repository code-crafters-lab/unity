package org.codecrafterslab.unity.oauth2.config;

import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.codecrafterslab.unity.oauth2.federation.FederatedIdentityIdTokenCustomizer;
import org.codecrafterslab.unity.oauth2.jose.Jwks;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Arrays;

@Configuration
public class TokenConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        ECKey ecKey = Jwks.generateEc();
        OctetSequenceKey octetSequenceKey = Jwks.generateSecret();
        JWKSet jwkSet = new JWKSet(Arrays.asList(rsaKey, ecKey, octetSequenceKey));
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer() {
        return new FederatedIdentityIdTokenCustomizer();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    OAuth2TokenGenerator<?> oauth2TokenGenerator(JwtEncoder jwtEncoder,
//                                                 OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer) {
//        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
//        jwtGenerator.setJwtCustomizer(jwtCustomizer);
//        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
////        accessTokenGenerator.setAccessTokenCustomizer(new FederatedIdentityAccessTokenCustomizer());
//        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
//        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, refreshTokenGenerator, jwtGenerator);
//    }

}
