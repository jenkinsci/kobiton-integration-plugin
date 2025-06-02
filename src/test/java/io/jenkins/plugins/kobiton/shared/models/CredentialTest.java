package io.jenkins.plugins.kobiton.shared.models;

import hudson.util.Secret;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CredentialTest {

    private static final String USERNAME = "username";
    private static final String API_KEY = "apiKey";

    @Test
    void constructorAndGettersWithSecretApiKey_ShouldReturnCorrectly() {
        Secret apiKey = Secret.fromString("mySecretApiKey");

        Credential credential = new Credential(USERNAME, apiKey);

        assertEquals(USERNAME, credential.getUsername());
        assertEquals(apiKey.getPlainText(), credential.getApiKey());
    }

    @Test
    void constructorAndGettersWithStringApiKey_ShouldReturnCorrectly() {
        Credential credential = new Credential(USERNAME, API_KEY);

        assertEquals(USERNAME, credential.getUsername());
        assertEquals(API_KEY, credential.getApiKey());
    }

    @Test
    void getCredentials_ShouldReturnCorrectly() {
        String expectedCredentials = USERNAME + ":" + API_KEY;

        Credential credential = new Credential(USERNAME, API_KEY);

        assertEquals(expectedCredentials, credential.getCredentials());
    }
}