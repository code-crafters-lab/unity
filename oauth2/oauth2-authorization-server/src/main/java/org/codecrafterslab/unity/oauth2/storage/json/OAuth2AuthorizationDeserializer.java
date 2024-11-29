package org.codecrafterslab.unity.oauth2.storage.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
public class OAuth2AuthorizationDeserializer extends JsonDeserializer<OAuth2Authorization> {
    static final TypeReference<Set<String>> STRING_SET = new TypeReference<Set<String>>() {
    };
    static final TypeReference<Map<String, Object>> STRING_OBJECT_MAP = new TypeReference<Map<String, Object>>() {
    };
    public static final String ID = "id";
    public static final String REGISTERED_CLIENT_ID = "registered_client_id";
    public static final String PRINCIPAL_NAME = "principal_name";
    public static final String AUTHORIZATION_GRANT_TYPE = "authorization_grant_type";
    public static final String AUTHORIZED_SCOPES = "authorized_scopes";

    private final RegisteredClientRepository registeredClientRepository;

    public OAuth2AuthorizationDeserializer(RegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }


    @Override
    public OAuth2Authorization deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode root = mapper.readTree(parser);
        return deserialize(parser, mapper, root);
    }

    private OAuth2Authorization deserialize(JsonParser parser, ObjectMapper mapper, JsonNode root) throws JsonParseException {
        String id = findStringValue(root, ID);
        String registeredClientId = findStringValue(root, REGISTERED_CLIENT_ID);
        String principalName = findStringValue(root, PRINCIPAL_NAME);
        String authorizationGrantType = findStringValue(root, AUTHORIZATION_GRANT_TYPE);

        Set<String> authorizedScopes = findAuthorizedScopes(root, mapper);

        OAuth2Authorization.Builder builder = getBuilder(registeredClientRepository, registeredClientId);

        builder.id(id).principalName(principalName)
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
                .authorizedScopes(authorizedScopes);
        return builder.build();
    }

    private OAuth2Authorization.Builder getBuilder(RegisteredClientRepository registeredClientRepository,
                                                   String registeredClientId) throws JsonParseException {
        RegisteredClient registeredClient = registeredClientRepository.findById(registeredClientId);
        if (registeredClient == null) {
            throw new DataRetrievalFailureException("The RegisteredClient with id '" + registeredClientId
                    + "' was not found in the RegisteredClientRepository.");
        }
        return OAuth2Authorization.withRegisteredClient(registeredClient);
    }

    private static String findStringValue(JsonNode jsonNode, String fieldName) {
        if (jsonNode == null) return null;
        JsonNode value = jsonNode.findValue(fieldName);
        return (value != null && value.isTextual()) ? value.asText() : null;
    }

    private static Set<String> findAuthorizedScopes(JsonNode jsonNode, ObjectMapper mapper) {
        if (jsonNode == null) return null;
        JsonNode value = jsonNode.findValue(AUTHORIZED_SCOPES);
        Set<String> result = new HashSet<>();
        if (value != null && value.isContainerNode() && value instanceof ArrayNode arrayNode) {
            Iterator<JsonNode> elements = arrayNode.elements();
            while (elements.hasNext()) {
                JsonNode node = elements.next();
                result.add(node.asText());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Authorized scopes: {}", StringUtils.collectionToCommaDelimitedString(result));
        }
        return Collections.unmodifiableSet(result);
    }

}
