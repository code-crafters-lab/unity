package org.codecrafterslab.unity.oauth2.client.dingtalk;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @time 2022/10/30 10:30
 */
public class DingTalkOAuth2User extends DefaultOAuth2User {
    public final static String NAME_ATTRIBUTE_KEY = "nick";

    public DingTalkOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        super(authorities, attributes, NAME_ATTRIBUTE_KEY);
    }

    /**
     * 用户的钉钉昵称
     *
     * @return 昵称
     */
    public String getNickName() {
        return getAttribute(NAME_ATTRIBUTE_KEY);
    }

    /**
     * 获取用户头像
     *
     * @return 头像URL
     */
    public String getAvatarUrl() {
        return getAttribute("avatarUrl");
    }

    /**
     * 用户的手机号
     *
     * @return 手机号
     */
    public String getMobile() {
        return getAttribute("mobile");
    }

    /**
     * 用户的 openId
     *
     * @return 用户在应用中的唯一标识
     */
    public String getOpenId() {
        return getAttribute("openId");
    }

    /**
     * 用户的 unionId
     *
     * @return 用户在企业中的唯一标识
     */
    public String getUnionId() {
        return getAttribute("unionId");
    }

    /**
     * 用户的个人邮箱
     *
     * @return 邮箱地址
     */
    public String getEmail() {
        return getAttribute("email");
    }

    /**
     * 手机号对应的国家号
     *
     * @return 国家号
     */
    public String getStateCode() {
        return getAttribute("stateCode");
    }

    /**
     * 用户登录时选择的企业
     *
     * @return 企业 ID
     */
    public String getCorpId() {
        return getAttribute("corpId");
    }

}
