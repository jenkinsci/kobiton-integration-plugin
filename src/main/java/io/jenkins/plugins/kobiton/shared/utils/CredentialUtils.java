package io.jenkins.plugins.kobiton.shared.utils;

import io.jenkins.plugins.kobiton.shared.models.Credential;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class CredentialUtils {
    private CredentialUtils() { throw new IllegalStateException("Utils class"); }

    /**
     * Encode credential to Base64
     *
     * @param credential credential
     * @return encoded credential
     */
    public static String encodeCredentials(Credential credential) {
        byte[] encodedBytes = Base64.getEncoder().encode(credential.getCredentials().getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}

