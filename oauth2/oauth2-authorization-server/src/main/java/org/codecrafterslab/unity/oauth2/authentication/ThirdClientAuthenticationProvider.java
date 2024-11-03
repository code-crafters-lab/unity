package org.codecrafterslab.unity.oauth2.authentication;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ThirdClientAuthenticationProvider implements AuthenticationProvider {
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";
    private final RegisteredClientRepository registeredClientRepository;
    private final RestTemplate restTemplate;
    private ClientRegistrationRepository clientRegistrationRepository;

    public ThirdClientAuthenticationProvider(RegisteredClientRepository registeredClientRepository,
                                             ClientRegistrationRepository clientRegistrationRepository,
                                             RestTemplateBuilder builder) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        this.registeredClientRepository = registeredClientRepository;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.restTemplate = builder.rootUri("https://oapi.dingtalk.com").build();
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT,
                "Client authentication failed: " + parameterName, ERROR_URI);
        throw new OAuth2AuthenticationException(error);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ThirdClientAuthenticationToken clientAuthentication = (ThirdClientAuthenticationToken) authentication;

        String clientId = clientAuthentication.getClientId();
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        String registrationId = registeredClient.getClientSettings().getSetting("settings.client.provider");
        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);

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

        // 1. 获取第三方应用令牌
        ClientAuth clientAuth;
        try {
            clientAuth = restTemplate.postForObject("https://api.dingtalk.com/v1.0/oauth2/accessToken",
                    new HashMap() {
                        {
                            put("appKey", clientRegistration.getClientId());
                            put("appSecret", clientRegistration.getClientSecret() + "");
                        }
                    }, ClientAuth.class);
        } catch (RestClientException e) {
            log.error("{}", e.getMessage());
            throw new OAuth2AuthenticationException("Failed to retrieve client authentication");
        }

        // 2. 通过免登码获取用户信息 https://open.dingtalk.com/document/isvapp/obtain-the-userid-of-a-user-by-using-the-log-free
        String code = clientAuthentication.getCode();
        Map<String, Object> map = restTemplate.postForObject("/topapi/v2/user/getuserinfo?access_token={accessToken}"
                , new HashMap<>() {{
                    put("code", code);
                }},
                Map.class, clientAuth.accessToken);

        Integer errcode = (Integer) map.get("errcode");
        if (errcode != null && errcode != 0) {
            throw new OAuth2AuthenticationException("Failed to retrieve user information from third party");
        }

        Object credentials = map.get("result");

        log.info("map: {}", credentials);
        // 3. 查询用户详情 https://open.dingtalk.com/document/isvapp/query-user-details

        return new OAuth2ClientAuthenticationToken(registeredClient, ClientAuthenticationMethod.NONE, credentials);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ThirdClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Data
    static class ClientAuth {
        private Integer expireIn;
        private String accessToken;
    }
}
