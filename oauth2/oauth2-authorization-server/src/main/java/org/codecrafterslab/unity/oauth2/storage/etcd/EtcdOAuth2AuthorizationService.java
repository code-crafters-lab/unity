package org.codecrafterslab.unity.oauth2.storage.etcd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Slf4j
public class EtcdOAuth2AuthorizationService implements OAuth2AuthorizationService, ETCDKeyPrefix {
    private final Client client;
    private final KV kv;
    private final Lease lease;
    private final RegisteredClientRepository registeredClientRepository;
    private final ObjectMapper objectMapper;


    public EtcdOAuth2AuthorizationService(RegisteredClientRepository registeredClientRepository, Client client,
                                          ObjectMapper objectMapper) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        Assert.notNull(client, "etcd client cannot be null");
        Assert.notNull(objectMapper, "objectMapper cannot be null");
        this.registeredClientRepository = registeredClientRepository;
        this.client = client;
        this.objectMapper = objectMapper;
        this.kv = client.getKVClient();
        this.lease = client.getLeaseClient();
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (log.isTraceEnabled()) log.trace("Saving authorization");
        try {
            // todo 配置控制授权有效时长
            LeaseGrantResponse lease = this.lease.grant(Duration.ofMinutes(30).getSeconds()).get();
            long leaseId = lease.getID();

            /* 1. 授权主体信息 */
            saveData(authorization, leaseId, authorization.getId());

            /* 2. 授权确认 state */
            String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
            if (StringUtils.hasText(state)) {
                saveData(state, leaseId, authorization.getId(), OAuth2ParameterNames.STATE, state);
            } else {
                deleteData(authorization.getId(), OAuth2ParameterNames.STATE);
            }

            /* 2.授权令牌信息 */
            saveTokens(authorization, Arrays.asList(OAuth2AuthorizationCode.class, OAuth2AccessToken.class,
                    OAuth2RefreshToken.class, OidcIdToken.class, OAuth2DeviceCode.class, OAuth2UserCode.class));


            /* 3.授权属性信息  */
            saveData(authorization.getAttributes(), leaseId, authorization.getId(), "attributes");
        } catch (InterruptedException | ExecutionException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }


    protected void saveTokens(OAuth2Authorization authorization,
                              List<Class<? extends AbstractOAuth2Token>> tokenTypes) throws ExecutionException,
            InterruptedException {
        String name;
        ByteSequence key;
        for (Class<? extends AbstractOAuth2Token> tokenType : tokenTypes) {
            OAuth2Authorization.Token<? extends AbstractOAuth2Token> token = authorization.getToken(tokenType);
            if (token == null) continue;
            name = getTokenKeyName(tokenType);
            key = ETCDKeyPrefix.getKey(ETCDKeyPrefix.OAUTH2_AUTHORIZE_PREFIX, authorization.getId(), name);
            this.saveToken(key, token);
        }
    }

    private void saveToken(ByteSequence tokenKey, @NonNull OAuth2Authorization.Token<? extends OAuth2Token> token) throws ExecutionException, InterruptedException {
        Assert.notNull(token, "token cannot be null");
        // 令牌无效或过期后删除
        if (token.isInvalidated() || token.isExpired()) {
            kv.delete(tokenKey, DeleteOption.builder().isPrefix(true).build());
        } else {
            byte[] bytes = writeData(token);
            String tokenValue = token.getToken().getTokenValue();
            Instant expiresAt = token.getToken().getExpiresAt();
            LeaseGrantResponse grant = lease.grant(Duration.between(Instant.now(), expiresAt).getSeconds()).get();
            PutOption.Builder putOpts = PutOption.builder().withLeaseId(grant.getID());
            ByteSequence key = ETCDKeyPrefix.getKey(tokenKey, tokenValue);
            kv.put(key, ByteSequence.from(bytes), putOpts.build());
        }
    }

    protected void saveData(Object data, long leaseId, String... keys) {
        if (data == null) return;
        byte[] bytes = writeData(data);
        if (bytes == null) return;
        PutOption.Builder putOpts = PutOption.builder();
        if (leaseId > 0) putOpts.withLeaseId(leaseId);
        ByteSequence key = ETCDKeyPrefix.getKey(ETCDKeyPrefix.OAUTH2_AUTHORIZE_PREFIX, keys);
//        TxnResponse txnResponse = kv.txn().If(new Cmp(key, Cmp.Op.EQUAL, CmpTarget.version(0)))
//                .Then(Op.put(key, ByteSequence.from(bytes), putOpts.build()))
//                .Else(Op.get(key, GetOption.builder().build()))
//                .commit().get();
//
//        if (txnResponse.isSucceeded()) {
//            return;
//        }
//        List<GetResponse> getResponses = txnResponse.getGetResponses();
        kv.put(key, ByteSequence.from(bytes), putOpts.build());
    }

    private void deleteData(String... keys) {
        ByteSequence key = ETCDKeyPrefix.getKey(ETCDKeyPrefix.OAUTH2_AUTHORIZE_PREFIX, keys);
        kv.delete(key, DeleteOption.builder().isPrefix(true).build());
    }


    @Override
    public void remove(OAuth2Authorization authorization) {
        if (log.isDebugEnabled()) {
            log.debug("Removing authorization: {}", authorization.getId());
        }
        ByteSequence key = ETCDKeyPrefix.getKey(ETCDKeyPrefix.OAUTH2_AUTHORIZE_PREFIX, authorization.getId());
        kv.delete(key, DeleteOption.builder().isPrefix(true).build());
    }

    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        log.info("Finding authorization by id: {}", id);
        ByteSequence key = ETCDKeyPrefix.getKey(ETCDKeyPrefix.OAUTH2_AUTHORIZE_PREFIX, id);
        GetOption.Builder getOpts = GetOption.builder().isPrefix(true);
        GetResponse response;
        try {
            response = kv.get(key, getOpts.build()).get();
        } catch (InterruptedException | ExecutionException e) {
            if (log.isErrorEnabled()) log.error(e.getLocalizedMessage(), e);
            return null;
        }
        if (response.getCount() == 0) return null;

        List<KeyValue> kvs = response.getKvs();

        // 1. 授权主信息获取
        KeyValue authorizationKeyValue = kvs.stream().filter(kv -> kv.getKey().equals(key)).findFirst().orElse(null);
        if (authorizationKeyValue == null) return null;
        OAuth2Authorization authorization = readData(authorizationKeyValue, OAuth2Authorization.class);
        if (authorization == null) return null;

        OAuth2Authorization.Builder builder = OAuth2Authorization.from(authorization);

        // 2. 授权附加信息获取
        kvs.stream().filter(kv -> !kv.getKey().equals(key)).forEach(kv -> {
            String k =
                    kv.getKey().toString().replace(key.toString(), "").replaceFirst(ByteSequence.NAMESPACE_DELIMITER.toString(StandardCharsets.UTF_8), "");
            if (k.equals("attributes")) {
                Map<String, Object> map = readData(kv, new TypeReference<>() {
                });
                builder.attributes(attrs -> attrs.putAll(map));
            } else if (!k.contains("state")) {
                OAuth2Authorization.Token<?> token = readData(kv, OAuth2Authorization.Token.class);
                OAuth2Token token1 = token.getToken();
                builder.token(token1);
            }

        });

//        String state = readData(maps, OAuth2ParameterNames.STATE, String.class);
//        if (StringUtils.hasText(state)) {
//            builder.attribute(OAuth2ParameterNames.STATE, state);
//        }

        return builder.build();
    }

    protected <T> T readData(KeyValue keyValue, Class<T> clazz) {
        String value = keyValue.getValue().toString();
        if (!StringUtils.hasText(value)) return null;
        try {
            return objectMapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            if (log.isErrorEnabled()) log.error(e.getLocalizedMessage());
        }
        return null;
    }

    protected <T> T readData(KeyValue keyValue, TypeReference<T> typeReference) {
        String value = keyValue.getValue().toString();
        if (!StringUtils.hasText(value)) return null;
        try {
            return objectMapper.readValue(value, typeReference);
        } catch (JsonProcessingException e) {
            if (log.isErrorEnabled()) log.error(e.getLocalizedMessage());
        }
        return null;
    }

    protected byte[] writeData(Object data) {
        byte[] bytes;
        try {
            if (data instanceof String s) {
                bytes = s.getBytes(StandardCharsets.UTF_8);
//                bytes = objectMapper.writeValueAsBytes(new RawValue(new SerializedString(s)));
            } else {
                bytes = objectMapper.writeValueAsBytes(data);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        if (log.isDebugEnabled()) {
            log.debug("Finding authorization by {}token: {}", tokenType != null ? tokenType.getValue() + " " : "",
                    token);
        }
        GetResponse response;
        GetOption.Builder getOpts = GetOption.builder().isPrefix(true).withKeysOnly(true);
        try {
            response = kv.get(ETCDKeyPrefix.OAUTH2_AUTHORIZE_PREFIX, getOpts.build()).get();
        } catch (InterruptedException | ExecutionException e) {
            if (log.isErrorEnabled()) log.error(e.getLocalizedMessage(), e);
            return null;
        }
        if (response.getCount() == 0) return null;
        List<KeyValue> kvs = response.getKvs();

        Pattern tokenKeyPattern = getTokenKeyPattern(tokenType, token);

        String authorizationId = kvs.stream()
                .map(KeyValue::getKey)
                .map(ByteSequence::toString)
                .map(tokenKeyPattern::matcher)
                .filter(Matcher::find)
                .map(matcher -> matcher.group(1))
                .findFirst().orElse(null);

        if (authorizationId == null) return null;
        return this.findById(authorizationId);
    }


    protected Pattern getTokenKeyPattern(@Nullable OAuth2TokenType tokenType, @Nullable String token) {
        String type = StringUtils.collectionToDelimitedString(Arrays.asList("(", tokenType == null ? ".*" :
                tokenType.getValue(), ")"), "");
        String value = StringUtils.collectionToDelimitedString(Arrays.asList("(", !StringUtils.hasText(token) ? ".*"
                : token, ")"), "");
        ByteSequence matchKey = ETCDKeyPrefix.getKey(OAUTH2_AUTHORIZE_PREFIX, "(.*)", type, value);
        if (log.isDebugEnabled()) {
            log.debug("TokenKeyPattern => {}", matchKey.toString());
        }
        return Pattern.compile(matchKey.toString());
    }

    protected Pattern getTokenKeyPattern() {
        return Pattern.compile(ETCDKeyPrefix.getKey(OAUTH2_AUTHORIZE_PREFIX, "(.*)", "(.*)", "(.*)").toString());
    }
}
