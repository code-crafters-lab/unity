package org.codecrafterslab.unity.oauth2.storage.etcd;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.Client;
import org.codecrafterslab.unity.oauth2.storage.json.OAuth2Module;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.List;

@Configuration
@ConditionalOnClass(Client.class)
public class ETCDConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Client etcdClient() {
        return Client.builder().endpoints("http://localhost:2379").build();
    }


    @Bean
    @ConditionalOnMissingBean
    ObjectMapper oauth2ObjectMapper(Jackson2ObjectMapperBuilder builder,
                                    RegisteredClientRepository registeredClientRepository) {
        ClassLoader classLoader = EtcdOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        securityModules.add(new OAuth2AuthorizationServerJackson2Module());
        securityModules.add(new OAuth2Module(registeredClientRepository));
        return builder.createXmlMapper(false).modules(securityModules).build();
    }

    @Bean
    @ConditionalOnMissingBean
    OAuth2AuthorizationService authorizationService(RegisteredClientRepository registeredClientRepository,
                                                    Client client, ObjectMapper oauth2ObjectMapper) {
        return new EtcdOAuth2AuthorizationService(registeredClientRepository, client, oauth2ObjectMapper);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public OAuth2AuthorizationConsentService authorizationConsentService(Client client,
//                                                                         ObjectMapper oauth2ObjectMapper) {
//        return new ETCDOAuth2AuthorizationConsentService(client, oauth2ObjectMapper);
//    }

}
