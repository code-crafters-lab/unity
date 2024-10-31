package org.codecrafterslab.unity.oauth2.customizer;

import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration;

import java.util.function.Consumer;

public class OidcProviderConfigurationEndpointCustomizer implements Consumer<OidcProviderConfiguration.Builder> {
    @Override
    public void accept(OidcProviderConfiguration.Builder builder) {
    }
}
