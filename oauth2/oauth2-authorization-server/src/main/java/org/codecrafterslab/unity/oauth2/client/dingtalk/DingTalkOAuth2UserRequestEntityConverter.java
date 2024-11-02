package org.codecrafterslab.unity.oauth2.client.dingtalk;

import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

/**
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @time 2022/10/30 10:43
 */
@Setter
public class DingTalkOAuth2UserRequestEntityConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {
    private String tokenHeaderName = "x-acs-dingtalk-access-token";

    @Override
    public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userRequest.getAccessToken().getTokenValue();
        headers.set(tokenHeaderName, token);
        URI uri =
                UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri()).build().toUri();
        return new RequestEntity<>(headers, HttpMethod.GET, uri);
    }

}
