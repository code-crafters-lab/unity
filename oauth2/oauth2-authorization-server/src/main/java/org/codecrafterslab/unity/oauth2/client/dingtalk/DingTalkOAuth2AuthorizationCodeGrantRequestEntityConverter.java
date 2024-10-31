package org.codecrafterslab.unity.oauth2.client.dingtalk;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/05/03 17:04
 */
public class DingTalkOAuth2AuthorizationCodeGrantRequestEntityConverter extends OAuth2AuthorizationCodeGrantRequestEntityConverter {

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        if (isDingTalk(authorizationCodeGrantRequest)) {
            return dingTalkConvert(authorizationCodeGrantRequest);
        }
        return super.convert(authorizationCodeGrantRequest);
    }


    private boolean isDingTalk(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        return authorizationCodeGrantRequest.getClientRegistration().getRegistrationId().equals("dingtalk");
    }

    private RequestEntity<DingTalkOAuth2TokenRequest> dingTalkConvert(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        OAuth2AuthorizationExchange authorizationExchange = authorizationGrantRequest.getAuthorizationExchange();

        DingTalkOAuth2TokenRequest.Builder builder = DingTalkOAuth2TokenRequest.builder();

        builder.clientId(clientRegistration.getClientId()).clientSecret(clientRegistration.getClientSecret()).grantType(authorizationGrantRequest.getGrantType().getValue());

        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType())) {
            builder.code(authorizationExchange.getAuthorizationResponse().getCode());
        } else if (AuthorizationGrantType.REFRESH_TOKEN.equals(clientRegistration.getAuthorizationGrantType())) {
            /* 获取存储的刷新令牌 */
            String refreshToken =
                    authorizationGrantRequest.getAuthorizationExchange().getAuthorizationRequest().getAttribute(
                            "refreshToken");
            builder.refreshToken(refreshToken);
        }

        URI uri =
                UriComponentsBuilder.fromUriString(authorizationGrantRequest.getClientRegistration().getProviderDetails().getTokenUri()).build().toUri();
        return new RequestEntity<>(builder.build(), headers, HttpMethod.POST, uri);
    }

}
