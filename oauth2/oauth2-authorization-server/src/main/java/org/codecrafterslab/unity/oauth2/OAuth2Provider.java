package org.codecrafterslab.unity.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

/**
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @time 2022/10/12 23:35
 */
public interface OAuth2Provider extends AuthProvider {

    /**
     * 默认重定向地址
     */
    String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

    String DEFAULT_ISSUER_URI = "{baseUrl}";

    /**
     * Create a new
     * {@link ClientRegistration.Builder
     * ClientRegistration.Builder} pre-configured with provider defaults.
     *
     * @param registrationId the registration-id used with the new builder
     * @return a builder instance
     */
    ClientRegistration.Builder getBuilder(String registrationId);

    /**
     * 获取 OAuth2 提供商描述信息
     *
     * @return 描述信息
     */
    String getDescription();

}
