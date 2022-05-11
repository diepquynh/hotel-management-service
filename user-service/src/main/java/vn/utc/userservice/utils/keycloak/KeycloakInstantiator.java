package vn.utc.userservice.utils.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KeycloakInstantiator {

    private final KeycloakConfiguration keycloakConfiguration;

    @Bean
    public Keycloak getInstance() {
        return KeycloakBuilder
                .builder()
                .serverUrl(keycloakConfiguration.getUrl())
                .realm(keycloakConfiguration.getRealm())
                .grantType("client_credentials")
                .clientId(keycloakConfiguration.getClientId())
                .clientSecret(keycloakConfiguration.getClientSecret())
                .build();
    }
}
