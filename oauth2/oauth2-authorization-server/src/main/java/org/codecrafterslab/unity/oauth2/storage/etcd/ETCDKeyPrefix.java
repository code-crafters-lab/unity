package org.codecrafterslab.unity.oauth2.storage.etcd;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.etcd.jetcd.ByteSequence;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public interface ETCDKeyPrefix {
    ByteSequence OAUTH2_PREFIX = getKey("oauth2");
    ByteSequence OAUTH2_CLIENT_PREFIX = getKey(OAUTH2_PREFIX, "client");
    ByteSequence OAUTH2_AUTHORIZE_PREFIX = getKey(OAUTH2_PREFIX, "authorize");
    ByteSequence OAUTH2_AUTHORIZE_CONSENT_PREFIX = getKey(OAUTH2_AUTHORIZE_PREFIX, "consent");

    static ByteSequence getKey(ByteSequence prefix, String... keys) {
        ByteSequence result = Arrays.stream(keys)
                .filter(StringUtils::hasText).map(key -> ByteSequence.NAMESPACE_DELIMITER.concat(ByteSequence.from(key, StandardCharsets.UTF_8)))
                .reduce(prefix, ByteSequence::concat);
        if (ByteSequence.EMPTY.equals(result)) return ByteSequence.NAMESPACE_DELIMITER;
        return result;
    }

    static ByteSequence getKey(String... keys) {
        return getKey(ByteSequence.EMPTY, keys);
    }

    default String getTokenKeyName(Class<? extends AbstractOAuth2Token> tokenType) {
        if (tokenType.isAssignableFrom(OAuth2AuthorizationCode.class)) {
            return OAuth2ParameterNames.CODE;
        } else if (tokenType.isAssignableFrom(OAuth2AccessToken.class)) {
            return OAuth2ParameterNames.ACCESS_TOKEN;
        } else if (tokenType.isAssignableFrom(OAuth2RefreshToken.class)) {
            return OAuth2ParameterNames.REFRESH_TOKEN;
        } else if (tokenType.isAssignableFrom(OidcIdToken.class)) {
            return OidcParameterNames.ID_TOKEN;
        } else if (tokenType.isAssignableFrom(OAuth2DeviceCode.class)) {
            return OAuth2ParameterNames.DEVICE_CODE;
        } else if (tokenType.isAssignableFrom(OAuth2UserCode.class)) {
            return OAuth2ParameterNames.USER_CODE;
        }
        return PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE.translate(tokenType.getSimpleName());
    }
}
