package org.codecrafterslab.unity.oauth2.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

public class ThirdCodeAuthenticationProvider implements AuthenticationProvider {
    private OAuth2AuthorizationService authorizationService;
    private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final HttpSecurity httpSecurity;
    private RegisteredClientRepository registeredClientRepository;

    public ThirdCodeAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                           OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                           HttpSecurity httpSecurity) {
        this.httpSecurity = httpSecurity;
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    public ThirdCodeAuthenticationProvider(HttpSecurity httpSecurity,
                                           RegisteredClientRepository registeredClientRepository) {
        this.httpSecurity = httpSecurity;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ThirdCodeAuthenticationToken authenticationToken = (ThirdCodeAuthenticationToken) authentication;

        // todo 查询后台指定供应的客户端是否存在
        RegisteredClient registeredClient =
                registeredClientRepository.findByClientId(authenticationToken.getClientId());

        if (registeredClient == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_client"));
        }

        // Ensure the client is configured to use this authorization grant type
        if (!registeredClient.getAuthorizationGrantTypes().contains(authenticationToken.getGrantType())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        ThirdCodeAuthenticationToken resultToken =
                new ThirdCodeAuthenticationToken(authenticationToken.getProvider(),
                        authenticationToken.getCode(), registeredClient,
                        (Authentication) authenticationToken.getPrincipal());
        // Ensure the client is authenticated

        // TODO Validate the code parameter

        // Generate the access token
        OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(resultToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(authenticationToken.getGrantType())
                .authorizationGrant(authenticationToken)
                .build();

        if (this.tokenGenerator == null) {
            this.tokenGenerator = httpSecurity.getSharedObject(OAuth2TokenGenerator.class);
        }

        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", null);
            throw new OAuth2AuthenticationException(error);
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), null);

        // Initialize the OAuth2Authorization
//        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
//                .principalName(clientPrincipal.getName())
//                .authorizationGrantType(authenticationToken.getGrantType());
//        if (generatedAccessToken instanceof ClaimAccessor) {
//            authorizationBuilder.token(accessToken, (metadata) ->
//                    metadata.put(
//                            OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
//                            ((ClaimAccessor) generatedAccessToken).getClaims())
//            );
//        } else {
//            authorizationBuilder.accessToken(accessToken);
//        }
//        OAuth2Authorization authorization = authorizationBuilder.build();

        // Save the OAuth2Authorization
//        if (this.authorizationService == null) {
//            this.authorizationService = httpSecurity.getSharedObject(OAuth2AuthorizationService.class);
//        }
//        this.authorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, resultToken, accessToken);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ThirdCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

}
