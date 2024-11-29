package org.codecrafterslab.unity.oauth2.storage.json.mixin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.codecrafterslab.unity.oauth2.client.dingtalk.DingTalkOAuth2User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.DEFAULT)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class DingTalkOAuth2UserMixin {

    @JsonCreator
    static DingTalkOAuth2User from(@JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
                                   @JsonProperty("attributes") Map<String, Object> attributes) {
        Map<String, Object> map = new HashMap<>();
        return new DingTalkOAuth2User(authorities, attributes);
    }

}
