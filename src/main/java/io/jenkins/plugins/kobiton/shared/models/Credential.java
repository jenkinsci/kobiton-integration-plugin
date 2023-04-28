package io.jenkins.plugins.kobiton.shared.models;

import hudson.util.Secret;

public class Credential {
    private final String username;
    private final Secret apiKey;

    public Credential(String username, Secret apiKey) {
        this.username = username;
        this.apiKey = apiKey;
    }

    public Credential(String username, String apiKey) {
        this.username = username;
        this.apiKey = Secret.fromString(apiKey);
    }

    public String getUsername() {
        return username;
    }

    public String getApiKey() {
        return apiKey.getPlainText();
    }

    public String getCredentials() {
        return username + ":" + apiKey;
    }
}
