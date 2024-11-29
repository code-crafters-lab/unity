package org.codecrafterslab.unity.oauth2.storage.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class OAuth2AuthorizationSerializer extends JsonSerializer<OAuth2Authorization> {

    @Override
    public void serialize(OAuth2Authorization authorization, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", authorization.getId());
        gen.writeStringField("registered_client_id", authorization.getRegisteredClientId());
        gen.writeStringField("principal_name", authorization.getPrincipalName());
        gen.writeStringField("authorization_grant_type", authorization.getAuthorizationGrantType().getValue());
        String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
        if (StringUtils.hasText(state)) gen.writeStringField("state", state);
        gen.writeFieldName("authorized_scopes");
        String[] scopes = authorization.getAuthorizedScopes().toArray(new String[]{});
        gen.writeArray(scopes, 0, scopes.length);
        gen.writeEndObject();
    }

    @Override
    public Class<OAuth2Authorization> handledType() {
        return OAuth2Authorization.class;
    }

}
