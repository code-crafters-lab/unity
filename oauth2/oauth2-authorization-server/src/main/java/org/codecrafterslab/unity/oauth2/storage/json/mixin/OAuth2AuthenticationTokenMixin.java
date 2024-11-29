package org.codecrafterslab.unity.oauth2.storage.json.mixin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class OAuth2AuthenticationTokenMixin {

    @JsonCreator
    public OAuth2AuthenticationTokenMixin(@JsonProperty("principal") OAuth2User principal,
                                          @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
                                          @JsonProperty("authorized_client_registration_id") String authorizedClientRegistrationId) {
    }

}
