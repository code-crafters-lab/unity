package org.codecrafterslab.unity.response.autoconfigure;

import org.codecrafterslab.unity.response.properties.ResponseProperties;

public interface ResponsePropertiesCustomizer {

    /**
     * 自定义响应配置
     *
     * @param properties 响应配置
     */
    void customize(ResponseProperties properties);

}
