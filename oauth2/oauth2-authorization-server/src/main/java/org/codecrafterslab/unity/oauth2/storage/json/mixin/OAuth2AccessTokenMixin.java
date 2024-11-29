package org.codecrafterslab.unity.oauth2.storage.json.mixin;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OAuth2AccessTokenMixin {

    @JsonCreator
    public OAuth2AccessTokenMixin(@JsonProperty("token_value") String tokenValue,
                                  @JsonProperty("issued_at") Instant issuedAt,
                                  @JsonProperty("expires_at") Instant expiresAt) {
    }
}
