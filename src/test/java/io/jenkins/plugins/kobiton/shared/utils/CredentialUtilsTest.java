package io.jenkins.plugins.kobiton.shared.utils;

import io.jenkins.plugins.kobiton.shared.models.Credential;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class CredentialUtilsTest {
    @Test
    void CredentialUtils_Constructor_IsPrivate() throws NoSuchMethodException {
        Constructor<CredentialUtils> constructor = CredentialUtils.class.getDeclaredConstructor();
        boolean expected = true;

        assertEquals(expected, Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void encodeCredentials_ValidCredential_ReturnsEncodedCredentials() {
        Credential credential = new Credential("username", "apiKey");
        String expected = "dXNlcm5hbWU6YXBpS2V5";

        String result = CredentialUtils.encodeCredentials(credential);

        assertEquals(expected, result);
    }
}