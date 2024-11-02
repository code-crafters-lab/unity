package org.codecrafterslab.unity.oauth2.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

@Slf4j
public class ThirdClientAuthenticationProvider implements AuthenticationProvider {
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";
    private final RegisteredClientRepository registeredClientRepository;

    public ThirdClientAuthenticationProvider(RegisteredClientRepository registeredClientRepository) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ThirdClientAuthenticationToken clientAuthentication = (ThirdClientAuthenticationToken) authentication;

        String clientId = clientAuthentication.getClientId();
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);

        if (registeredClient == null) {
            throwInvalidClient("client_id");
        }

        if (log.isTraceEnabled()) {
            log.trace("Retrieved registered client");
        }

        if (log.isTraceEnabled()) {
            log.trace("Authenticated public client");
        }

        // todo 根据临时授权码获取第三方用户信息
        // 授权信息存储第三方临时授权码

        return new OAuth2ClientAuthenticationToken(registeredClient, ClientAuthenticationMethod.NONE, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ThirdClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT,
                "Client authentication failed: " + parameterName, ERROR_URI);
        throw new OAuth2AuthenticationException(error);
    }
}
