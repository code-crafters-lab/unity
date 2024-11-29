package org.codecrafterslab.unity.oauth2.storage.json.mixin;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.codecrafterslab.unity.oauth2.storage.json.OAuth2AuthorizationSerializer;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonSerialize(using = OAuth2AuthorizationSerializer.class)
public abstract class OAuth2AuthorizationMixin {

}
