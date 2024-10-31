package org.codecrafterslab.unity.oauth2.customizer;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;

import java.util.*;
import java.util.function.Function;

public class OidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {


    private static Map<String, Object> getClaimsRequestedByScope(Map<String, Object> claims,
                                                                 Set<String> requestedScopes) {
        Set<String> scopeRequestedClaimNames = new HashSet<>(32);
        scopeRequestedClaimNames.add(StandardClaimNames.SUB);

        if (requestedScopes.contains(OidcScopes.OPENID)) {
            scopeRequestedClaimNames.addAll(Arrays.asList("openid", "union_id"));
        }

        if (requestedScopes.contains(OidcScopes.ADDRESS)) {
            scopeRequestedClaimNames.add(StandardClaimNames.ADDRESS);
        }
        if (requestedScopes.contains(OidcScopes.EMAIL)) {
            scopeRequestedClaimNames.addAll(Arrays.asList(
                    StandardClaimNames.EMAIL,
                    StandardClaimNames.EMAIL_VERIFIED
            ));
        }
        if (requestedScopes.contains(OidcScopes.PHONE)) {
            scopeRequestedClaimNames.addAll(Arrays.asList(
                    StandardClaimNames.PHONE_NUMBER,
                    StandardClaimNames.PHONE_NUMBER_VERIFIED
            ));
        }
        if (requestedScopes.contains(OidcScopes.PROFILE)) {
            scopeRequestedClaimNames.addAll(Arrays.asList(
                    StandardClaimNames.NAME,
                    StandardClaimNames.FAMILY_NAME,
                    StandardClaimNames.GIVEN_NAME,
                    StandardClaimNames.MIDDLE_NAME,
                    StandardClaimNames.NICKNAME,
                    StandardClaimNames.PREFERRED_USERNAME,
                    StandardClaimNames.PROFILE,
                    StandardClaimNames.PICTURE,
                    StandardClaimNames.WEBSITE,
                    StandardClaimNames.GENDER,
                    StandardClaimNames.BIRTHDATE,
                    StandardClaimNames.ZONEINFO,
                    StandardClaimNames.LOCALE,
                    StandardClaimNames.UPDATED_AT
            ));
        }

        if (requestedScopes.contains("dingtalk")) {
            scopeRequestedClaimNames.addAll(Arrays.asList("dingtalk_user_id", "dingtalk_union_id"));
        }

        Map<String, Object> requestedClaims = new HashMap<>(claims);
        requestedClaims.keySet().removeIf((claimName) -> !scopeRequestedClaimNames.contains(claimName));

        return requestedClaims;
    }

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
        OAuth2Authorization authorization = context.getAuthorization();
        OAuth2AccessToken accessToken = context.getAccessToken();
        OidcIdToken idToken = Objects.requireNonNull(authorization.getToken(OidcIdToken.class)).getToken();
        Map<String, Object> scopeRequestedClaims = getClaimsRequestedByScope(idToken.getClaims(),
                accessToken.getScopes());

//        OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
//        JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
//        return new OidcUserInfo(principal.getToken().getClaims());
        return new OidcUserInfo(scopeRequestedClaims);
    }
}
