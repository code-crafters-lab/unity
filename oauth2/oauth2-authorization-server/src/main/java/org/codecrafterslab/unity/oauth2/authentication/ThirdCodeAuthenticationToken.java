package org.codecrafterslab.unity.oauth2.authentication;

import lombok.Getter;
import org.codecrafterslab.unity.oauth2.AuthProvider;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;

@Getter
public class ThirdCodeAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public static final AuthorizationGrantType AUTHORIZATION_CODE =
            new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:code");
    /**
     * 授权提供商
     */
    private final AuthProvider provider;
    /**
     * 授权客户端
     */
    private final String clientId;
    /**
     * 临时授权码
     */
    private final String code;
    /**
     * 注册的客户端
     */
    private final RegisteredClient registeredClient;


    public ThirdCodeAuthenticationToken(AuthProvider provider, String clientId, String code,
                                        Authentication clientPrincipal,
                                        @Nullable Map<String, Object> additionalParameters) {
        super(AUTHORIZATION_CODE, clientPrincipal, additionalParameters);
        Assert.notNull(provider, "provider cannot be null");
        Assert.hasText(provider.getName(), "provider name cannot be empty");
        Assert.hasText(clientId, "clientId cannot be empty");
        Assert.hasText(code, "code cannot be empty");
        this.provider = provider;
        this.clientId = clientId;
        this.code = code;
        this.registeredClient = null;
    }

    public ThirdCodeAuthenticationToken(AuthProvider provider, String code,
                                        RegisteredClient registeredClient,
                                        Authentication clientPrincipal) {
        super(AUTHORIZATION_CODE, clientPrincipal, Collections.emptyMap());
        Assert.notNull(code, "code cannot be null");
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        this.provider = provider;
        this.clientId = registeredClient.getClientId();
        this.code = code;
        this.registeredClient = registeredClient;
        setAuthenticated(true);
    }
}
