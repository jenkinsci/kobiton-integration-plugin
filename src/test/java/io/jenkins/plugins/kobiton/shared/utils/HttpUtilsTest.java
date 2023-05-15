package io.jenkins.plugins.kobiton.shared.utils;

import io.jenkins.plugins.kobiton.shared.models.Credential;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpUtilsTest {
    private final String url = "https://i.love.kobiton.very/much";
    private final Credential credential = new Credential("username", "apiKey");

    @Test
    void HttpUtils_Constructor_ShouldBePrivate() throws NoSuchMethodException {
        Constructor<HttpUtils> constructor = HttpUtils.class.getDeclaredConstructor();
        boolean expected = true;

        assertEquals(expected, Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void createAuthHeader_ValidCredentialsGiven_ShouldReturnHttpRequestBuilderWithAuthorizationHeader() {
        HttpRequest.Builder builder = HttpUtils.createAuthHeader(url, credential);
        HttpRequest request = builder.build();

        assertEquals("Basic " + CredentialUtils.encodeCredentials(credential), request.headers().firstValue("Authorization").orElse(null));
    }

    @Test
    void createGetRequest_ValidCredentialsAndUrlGiven_ShouldReturnHttpGetRequestWithAuthorizationHeader() {
        HttpRequest request = HttpUtils.createGetRequest(url, credential);

        assertEquals("GET", request.method());
        assertEquals("Basic " + CredentialUtils.encodeCredentials(credential), request.headers().firstValue("Authorization").orElse(null));
    }

    @Test
    void createPostRequest_ValidCredentialsUrlAndBodyGiven_ShouldReturnHttpPostRequestWithAuthorizationAndContentTypeHeaders() {
        String body = "request body";

        HttpRequest request = HttpUtils.createPostRequest(url, credential, body);
        String expectedBody = request.bodyPublisher().map(StringSubscriber::wrapBodyPublisher).orElse(null);

        assertEquals("POST", request.method());
        assertEquals("Basic " + CredentialUtils.encodeCredentials(credential), request.headers().firstValue("Authorization").orElse(null));
        assertEquals("application/json", request.headers().firstValue("Content-Type").orElse(null));
        assertEquals(expectedBody, body);
    }

    @Test
    void createPutRequest_ValidUrlAndFileContent_ShouldReturnHttpPutRequestWithHeadersAndBody() {
        String preSignedUrl = "https://upload.your.file.here/please";
        String stringContent = "file content";
        byte[] fileContent = stringContent.getBytes();

        HttpRequest request = HttpUtils.createPutRequest(preSignedUrl, fileContent);
        String expectedStringBody = request.bodyPublisher().map(StringSubscriber::wrapBodyPublisher).orElse(null);

        assertEquals("PUT", request.method());
        assertEquals("unsaved=true", request.headers().firstValue("x-amz-tagging").orElse(null));
        assertEquals("application/octet-stream", request.headers().firstValue("Content-Type").orElse(null));
        assertEquals(expectedStringBody, stringContent);
    }
}