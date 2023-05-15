package io.jenkins.plugins.kobiton.shared.models;

import hudson.util.Secret;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialTest {
    private final String username = "username";
    private final String apiKey = "apiKey";
    @Test
    void ConstructorAndGettersWithSecretApiKey_ShouldReturnCorrectly() {
        Secret apiKey = Secret.fromString("mySecretApiKey");

        Credential credential = new Credential(username, apiKey);

        assertEquals(username, credential.getUsername());
        assertEquals(apiKey.getPlainText(), credential.getApiKey());
    }

    @Test
    void ConstructorAndGettersWithStringApiKey_ShouldReturnCorrectly() {
        Credential credential = new Credential(username, apiKey);

        assertEquals(username, credential.getUsername());
        assertEquals(apiKey, credential.getApiKey());
    }

    @Test
    void getCredentials_ShouldReturnCorrectly() {
        String expectedCredentials = username + ":" + apiKey;

        Credential credential = new Credential(username, apiKey);

        assertEquals(expectedCredentials, credential.getCredentials());
    }
}