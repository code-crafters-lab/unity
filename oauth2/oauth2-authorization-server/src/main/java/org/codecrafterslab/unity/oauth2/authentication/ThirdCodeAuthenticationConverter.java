package org.codecrafterslab.unity.oauth2.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.codecrafterslab.unity.oauth2.AuthProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.codecrafterslab.unity.oauth2.authentication.ThirdCodeAuthenticationToken.AUTHORIZATION_CODE;

@Setter
public class ThirdCodeAuthenticationConverter implements AuthenticationConverter {
    public static final String PROVIDER_NAME_KEY = "provider_name";

    private Converter<String, AuthProvider> authProviderConverter = name -> () -> name;
    private RegisteredClientRepository registeredClientRepository;

    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) parameters.add(key, value);
        });
        return parameters;
    }

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AUTHORIZATION_CODE.getValue().equals(grantType)) return null;

        MultiValueMap<String, String> parameters = getParameters(request);

        // provider (REQUIRED)
        String providerName = parameters.getFirst(PROVIDER_NAME_KEY); // <2>
        if (!StringUtils.hasText(providerName) || parameters.get(PROVIDER_NAME_KEY).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }
        AuthProvider provider = authProviderConverter.convert(providerName);

        // client_id (REQUIRED)
        String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID); // <2>
        if (!StringUtils.hasText(clientId) ||
                parameters.get(OAuth2ParameterNames.CLIENT_ID).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        // code (REQUIRED)
        String code = parameters.getFirst(OAuth2ParameterNames.CODE); // <2>
        if (!StringUtils.hasText(code) ||
                parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        // 用户未登录则是匿名认证
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        return new ThirdCodeAuthenticationToken(provider, clientId, code, clientPrincipal,
                additionalParameters);
    }

}
