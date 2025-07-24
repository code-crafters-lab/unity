package org.codecrafterslab.unity.response.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/05/06 21:37
 */
@Data
@ConfigurationProperties(prefix = "unity.response.wrapper")
public class ResponseWrapperProperties {

    /**
     * <p>是否启用响应结果自动包装，默认 true</p>
     * <p>局部控制请使用 {@link org.codecrafterslab.unity.response.annotation.ResponseResult} 注解</p>
     */
    private Boolean enable = true;

    /**
     * 全局包装需要忽略的类名称（完全限定名）
     *
     * <p>1. 方法所在的类</p>
     * <p>2. 方法返回类型</p>
     * <p>3. 方法参数 body 的类型</p>
     */
    private Set<String> ignoredClass = new HashSet<>();

    /**
     * 是否 springdoc 项目，若是则自动添加忽略的类
     */
    private Boolean springdoc = false;

    /**
     * 是否 springfox 项目，若是则自动添加忽略的类
     */
    private Boolean springfox = false;

    public void setSpringdoc(Boolean springdoc) {
        this.springdoc = springdoc;
        List<String> list = Arrays.asList(
                "org.springdoc.webmvc.api.OpenApiWebMvcResource",
                "org.springdoc.webmvc.api.OpenApiActuatorResource"
        );
        if (springdoc) {
            this.ignoredClass.addAll(list);
        } else {
            this.ignoredClass.removeAll(list);
        }
    }

    public void setSpringfox(Boolean springfox) {
        this.springfox = springfox;
        List<String> list = Arrays.asList(
                "springfox.documentation.swagger.web.ApiResourceController",
                "springfox.documentation.spring.web.json.Json"
        );
        if (springfox) {
            this.ignoredClass.addAll(list);
        } else {
            this.ignoredClass.removeAll(list);
        }
    }
}
