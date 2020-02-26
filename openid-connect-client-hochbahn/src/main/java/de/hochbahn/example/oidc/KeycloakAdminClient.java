package de.hochbahn.example.oidc;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeycloakAdminClient {

    public static void main(String[] args) {
        // User "javaland" needs at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
        String clientId = "admin-cli";
        String realm = "mobi-platform";
        Keycloak keycloak = KeycloakBuilder.builder() //
                .serverUrl("http://localhost:8180/auth") //
                .realm("master") //
                .grantType(OAuth2Constants.PASSWORD) //
                .clientId(clientId) //
                //.clientSecret(clientSecret) //
                .username("admin") //
                .password("admin") //
                .build();
        System.out.println(keycloak.serverInfo().getInfo().getSystemInfo().getVersion());

        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername("tester1");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("tom+tester1@tdlabs.local");
        user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userRessource = realmResource.users();

        // List users
        List<UserRepresentation> users = userRessource.list();
        users.stream().map(UserRepresentation::getUsername).forEach(System.out::println);

        // Create user (requires manage-users role)
        Response response = userRessource.create(user);
        if(response.getStatus() != 201) {
            System.out.println(response.getStatus() + " " + response.getStatusInfo());
            return;
        }
        System.out.println("Repsonse: " + response.getStatusInfo());
        System.out.println(response.getLocation());
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        System.out.printf("User created with userId: %s%n", userId);

        // Get realm role "tester" (requires view-realm role)
        RoleRepresentation testerRealmRole = realmResource.roles()//
                .get("user").toRepresentation();

        // Assign realm role tester to user
        userRessource.get(userId).roles().realmLevel() //
                .add(Arrays.asList(testerRealmRole));

        // Get role
        RoleRepresentation userClientRole = realmResource //
                .roles().get("user").toRepresentation();

        // Assign client level role to user
        userRessource.get(userId).roles()
                .realmLevel()
                .add(Arrays.asList(userClientRole));

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(true);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue("test");

        // Set password credential
        userRessource.get(userId).resetPassword(passwordCred);
    }

}
