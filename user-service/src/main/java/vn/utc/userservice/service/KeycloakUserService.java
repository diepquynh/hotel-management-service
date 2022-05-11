package vn.utc.userservice.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import vn.utc.userservice.utils.keycloak.KeycloakConfiguration;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final KeycloakConfiguration keycloakConfiguration;
    private final Keycloak keycloakInstance;

    private RealmResource getKeyCloakInstanceWithRealm() {
        return keycloakInstance.realm(keycloakConfiguration.getRealm());
    }

    public Integer createUser(UserRepresentation userRepresentation) {
        Response response = getKeyCloakInstanceWithRealm().users().create(userRepresentation);
        return response.getStatus();
    }

    public void updateUser(UserRepresentation userRepresentation) {
        getKeyCloakInstanceWithRealm().users().get(userRepresentation.getId()).update(userRepresentation);
    }

    public List<UserRepresentation> readUserByUsername(String username) {
        return getKeyCloakInstanceWithRealm().users().search(username);
    }

    public UserRepresentation readUser(String authId) {
        try {
            UserResource userResource = getKeyCloakInstanceWithRealm().users().get(authId);
            return userResource.toRepresentation();
        } catch (Exception e) {
            throw new EntityNotFoundException("User not found under given ID");
        }
    }
}
