package org.codecrafterslab.unity.response.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2020/09/24 12:22
 */
@Data
@ConfigurationProperties(prefix = "unity.response")
public class ResponseProperties {

    /**
     * 异常响应类型
     */
    private ExceptionType exceptionType;

    /**
     * 响应结果包装配置
     */
    @NestedConfigurationProperty
    private ResponseWrapperProperties wrapper = new ResponseWrapperProperties();

    /**
     * Result 序列化名称配置
     */
    @NestedConfigurationProperty
    private ResultJsonProperties result = new ResultJsonProperties();

}


