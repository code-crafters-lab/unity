package org.codecrafterslab.unity.oauth2.client.dingtalk;

import lombok.Builder;
import lombok.Data;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/05/03 22:42
 */
@Data
@Builder(builderClassName = "Builder")
public class DingTalkOAuth2TokenRequest {

    private String clientId;

    private String clientSecret;

    private String code;

    private String refreshToken;

    private String grantType;
}
