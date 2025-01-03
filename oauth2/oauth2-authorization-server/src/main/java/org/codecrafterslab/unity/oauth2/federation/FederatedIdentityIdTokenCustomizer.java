package org.codecrafterslab.unity.oauth2.federation;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An {@link OAuth2TokenCustomizer} to map claims from a federated identity to
 * the {@code id_token} produced by this authorization server.
 *
 * @author Steve Riesenberg
 * @since 1.1
 */
public final class FederatedIdentityIdTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final Set<String> ID_TOKEN_CLAIMS = Set.of(IdTokenClaimNames.ISS, IdTokenClaimNames.SUB,
            IdTokenClaimNames.AUD, IdTokenClaimNames.EXP, IdTokenClaimNames.IAT, IdTokenClaimNames.AUTH_TIME,
            IdTokenClaimNames.NONCE, IdTokenClaimNames.ACR, IdTokenClaimNames.AMR, IdTokenClaimNames.AZP,
            IdTokenClaimNames.AT_HASH, IdTokenClaimNames.C_HASH);

    @Override
    public void customize(JwtEncodingContext context) {
        // todo 根据配置自动获取
        context.getJwsHeader().type("JWT");
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            Map<String, Object> thirdPartyClaims = extractClaims(context.getPrincipal());
            context.getClaims().claims(existingClaims -> {
                // Remove conflicting claims set by this authorization server
                existingClaims.keySet().forEach(thirdPartyClaims::remove);

                // Remove standard id_token claims that could cause problems with clients
                ID_TOKEN_CLAIMS.forEach(thirdPartyClaims::remove);

                // Add all other claims directly to id_token
                existingClaims.putAll(thirdPartyClaims);
            });
        }
    }

    private Map<String, Object> extractClaims(Authentication authentication) {
        Map<String, Object> claims;
        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            OidcIdToken idToken = oidcUser.getIdToken();
            claims = idToken.getClaims();
        } else if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            claims = oauth2User.getAttributes();
        } else {
            // todo 获取用户细腻
            claims = new HashMap<>();
            claims.put(StandardClaimNames.NICKNAME, "coffee377");
            claims.put("job_number", "0634");
            claims.put("openid", "2");
            claims.put("union_id", "1");
            claims.put(StandardClaimNames.PREFERRED_USERNAME, "吴玉杰");
            claims.put(StandardClaimNames.PICTURE, "https://gw.alipayobjects" +
                    ".com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        }

        return new HashMap<>(claims);
    }

}
