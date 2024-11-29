package org.codecrafterslab.unity.oauth2.storage.json.mixin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.security.oauth2.core.OAuth2Token;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OAuth2AuthorizationTokenMixin<T extends OAuth2Token> {

    @JsonCreator
    public OAuth2AuthorizationTokenMixin(@JsonProperty("token") T token, @JsonProperty("metadata") Map<String,
            Object> metadata) {
    }
}
