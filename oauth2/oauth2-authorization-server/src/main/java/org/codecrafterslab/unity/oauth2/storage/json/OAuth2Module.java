package org.codecrafterslab.unity.oauth2.storage.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.codecrafterslab.unity.oauth2.storage.json.mixin.OAuth2AuthorizationCodeMixin;
import org.codecrafterslab.unity.oauth2.storage.json.mixin.OAuth2AuthorizationMixin;
import org.codecrafterslab.unity.oauth2.storage.json.mixin.OAuth2AuthorizationTokenMixin;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/07/23 23:49
 */
public class OAuth2Module extends SimpleModule {
    private final RegisteredClientRepository registeredClientRepository;


    public OAuth2Module(RegisteredClientRepository registeredClientRepository) {
        super(OAuth2Module.class.getName(), new Version(1, 0, 0, null, null, null));
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void setupModule(SetupContext context) {
        setMixInAnnotation(OAuth2Authorization.class, OAuth2AuthorizationMixin.class);
        addDeserializer(OAuth2Authorization.class, new OAuth2AuthorizationDeserializer(registeredClientRepository));

        setMixInAnnotation(OAuth2Authorization.Token.class, OAuth2AuthorizationTokenMixin.class);
        setMixInAnnotation(OAuth2AuthorizationCode.class, OAuth2AuthorizationCodeMixin.class);
//        setMixInAnnotation(OAuth2AuthenticationToken.class, OAuth2AuthenticationTokenMixin.class);

        super.setupModule(context);
    }
}
