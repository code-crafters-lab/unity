package org.codecrafterslab.unity.oauth2;


/**
 * 认证提供商
 *
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @time 2022/09/23 08:51
 */
@FunctionalInterface
public interface AuthProvider {

    /**
     * @return 提供商名称
     */
    String getName();

}
