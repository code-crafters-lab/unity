package org.codecrafterslab.unity.oauth2.authentication;

import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * An {@link Authentication} implementation used for the OAuth 2.0 Third Client Authorization Code
 */
@Getter
public class ThirdCodeAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public static final AuthorizationGrantType THIRD_AUTHORIZATION_CODE =
            new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:code");

    /**
     * 临时授权码
     */
    private final String code;

    public ThirdCodeAuthenticationToken(String code, Authentication clientPrincipal,
                                        @Nullable Map<String, Object> additionalParameters) {
        super(THIRD_AUTHORIZATION_CODE, clientPrincipal, additionalParameters);
        this.code = code;
        super.setAuthenticated(clientPrincipal.isAuthenticated());
    }

}
