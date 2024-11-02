package org.codecrafterslab.unity.oauth2.authentication;

import lombok.Getter;
import org.codecrafterslab.unity.oauth2.AuthProvider;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Getter
public class ThirdClientAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

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

    public ThirdClientAuthenticationToken(AuthProvider provider, String clientId, String code,
                                          Authentication clientPrincipal,
                                          @Nullable Map<String, Object> additionalParameters) {
        super(AUTHORIZATION_CODE, clientPrincipal, additionalParameters);
        Assert.notNull(provider, "provider cannot be null");
        Assert.hasText(clientId, "clientId cannot be empty");
        Assert.hasText(code, "code cannot be empty");
        this.provider = provider;
        this.clientId = clientId;
        this.code = code;
        super.setAuthenticated(false);
    }

    public static ThirdClientAuthenticationToken unauthenticated(AuthProvider provider, String clientId, String code,
                                                                 @Nullable Map<String, Object> additionalParameters) {
        AnonymousAuthenticationToken anonymousAuthenticationToken =
                new AnonymousAuthenticationToken(provider.getName(), StringUtils.arrayToDelimitedString(new Object[]{
                        clientId, code}, ":"), List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        return new ThirdClientAuthenticationToken(provider, clientId, code, anonymousAuthenticationToken,
                additionalParameters);
    }

    public static ThirdClientAuthenticationToken authenticated(AuthProvider provider, String clientId, String code,
                                                               Authentication clientPrincipal, @Nullable Map<String,
            Object> additionalParameters) {
        ThirdClientAuthenticationToken thirdCodeAuthenticationToken = new ThirdClientAuthenticationToken(provider,
                clientId, code, clientPrincipal, additionalParameters);
        thirdCodeAuthenticationToken.setAuthenticated(true);
        return thirdCodeAuthenticationToken;
    }
}
