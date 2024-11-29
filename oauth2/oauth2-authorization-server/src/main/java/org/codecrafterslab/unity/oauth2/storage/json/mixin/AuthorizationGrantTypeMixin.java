package org.codecrafterslab.unity.oauth2.storage.json.mixin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class AuthorizationGrantTypeMixin {

    @JsonProperty("value")
    String value;

    @JsonCreator
    public AuthorizationGrantTypeMixin(@JsonProperty("value") String value) {
        this.value = value;
    }
}
