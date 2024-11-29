package org.codecrafterslab.unity.oauth2.storage.etcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

public class ETCDOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService, DisposableBean {
    private final Client client;
    private final ObjectMapper mapper;

    public ETCDOAuth2AuthorizationConsentService(Client client, ObjectMapper objectMapper) {
        this.client = client;
        this.mapper = objectMapper;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        ByteSequence key = getAuthorizationConsentKey(authorizationConsent);
//        this.client.getKVClient().txn().If(new Compare(key, Compare.Op.NOT_EXIST)).Then(Put(key, consent)).Commit();
    }

    protected ByteSequence getAuthorizationConsentKey(String registeredClientId, String principalName) {
        return ETCDKeyPrefix.getKey(ETCDKeyPrefix.OAUTH2_AUTHORIZE_CONSENT_PREFIX,
                registeredClientId, principalName);
    }

    protected ByteSequence getAuthorizationConsentKey(OAuth2AuthorizationConsent authorizationConsent) {
        return getAuthorizationConsentKey(authorizationConsent.getRegisteredClientId(),
                authorizationConsent.getPrincipalName());
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        ByteSequence key = getAuthorizationConsentKey(authorizationConsent);
        this.client.getKVClient().delete(key);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        ByteSequence authorizationConsentKey = getAuthorizationConsentKey(registeredClientId, principalName);
//        GetResponse getResponse = this.client.getKVClient().get(authorizationConsentKey).get();

        return null;
    }


    @Override
    public void destroy() throws Exception {
        client.close();
    }
}
