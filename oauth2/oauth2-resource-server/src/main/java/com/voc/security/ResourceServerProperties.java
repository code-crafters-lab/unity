package com.voc.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2020/09/24 15:47
 */
@Data
@ConfigurationProperties(prefix = "api.security.res-server")
public class ResourceServerProperties {

    /**
     * 不需要验证权限的地址
     * @deprecated  use api.security.ignore instead
     */
    @Deprecated
    private List<String> ignore;

    /**
     * OAuth 2.0 endpoint through which token introspection is accomplished.
     */
    private String introspectionUri;

    /**
     * Client id used to authenticate with the token introspection endpoint.
     */
    private String clientId;

    /**
     * Client secret used to authenticate with the token introspection endpoint.
     */
    private String clientSecret;


}
