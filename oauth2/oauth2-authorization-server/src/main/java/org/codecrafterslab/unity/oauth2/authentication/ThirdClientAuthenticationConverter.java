package org.codecrafterslab.unity.oauth2.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.codecrafterslab.unity.oauth2.AuthProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Setter
public class ThirdClientAuthenticationConverter implements AuthenticationConverter {
    public static final String PROVIDER_NAME_KEY = "provider";
    private Converter<String, AuthProvider> authProviderConverter = name -> () -> name;


    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!ThirdCodeAuthenticationToken.THIRD_AUTHORIZATION_CODE.getValue().equals(grantType)) return null;

        MultiValueMap<String, String> parameters = ThirdCodeUtils.getParameters(request);

        // client_id (REQUIRED)
        String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID); // <2>
        if (!StringUtils.hasText(clientId) || parameters.get(OAuth2ParameterNames.CLIENT_ID).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        // code (REQUIRED)
        String code = parameters.getFirst(OAuth2ParameterNames.CODE); // <2>
        if (!StringUtils.hasText(code) || parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        // provider (REQUIRED)
        String providerName = parameters.getFirst(PROVIDER_NAME_KEY); // <2>
        if (!StringUtils.hasText(providerName) || parameters.get(PROVIDER_NAME_KEY).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }
        AuthProvider provider = authProviderConverter.convert(providerName);
        assert provider != null;

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return ThirdClientAuthenticationToken.unauthenticated(provider, clientId, code, additionalParameters);
        }

        return ThirdClientAuthenticationToken.authenticated(provider, clientId, code, authentication,
                additionalParameters);

    }
}
