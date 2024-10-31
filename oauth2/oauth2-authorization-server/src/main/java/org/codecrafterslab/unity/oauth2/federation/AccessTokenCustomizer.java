package org.codecrafterslab.unity.oauth2.federation;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Map;

public class AccessTokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        context.getClaims().claims(claims -> customize(context, claims));
    }

    private static void customize(OAuth2TokenContext tokenContext, Map<String, Object> claims) {
        // TODO: customize the access token
    }

}
