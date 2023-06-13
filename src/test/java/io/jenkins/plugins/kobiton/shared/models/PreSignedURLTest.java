package io.jenkins.plugins.kobiton.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreSignedURLTest {
    private final String appPath = "/path/to/app";
    private final String url = "https://i.love.kobiton/very/much";

    @Test
    void ConstructorAndGetters_ShouldReturnCorrectly() {
        PreSignedURL preSignedURL = new PreSignedURL(appPath, url);

        assertEquals(appPath, preSignedURL.appPath());
        assertEquals(url, preSignedURL.url());
    }

    @Test
    void equalsAndHashCode_ShouldReturnCorrectly() {
        PreSignedURL url1 = new PreSignedURL(appPath, url);
        PreSignedURL url2 = new PreSignedURL(appPath, url);
        PreSignedURL url3 = new PreSignedURL("/other/path", url);
        PreSignedURL url4 = new PreSignedURL(appPath, "this-url-is-weird");
        PreSignedURL url5 = null;
        Object url6 = new Object();

        assertEquals(url1, url1);
        assertEquals(url1, url2);
        assertNotEquals(url1, url3);
        assertNotEquals(url1, url4);
        assertNotEquals(url1, url5);
        assertNotEquals(url1, url6);
        assertEquals(url1.hashCode(), url2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectly() {
        PreSignedURL preSignedURL = new PreSignedURL(appPath, url);
        String expectedToString = "{appPath='/path/to/app', url='https://i.love.kobiton/very/much'}";

        assertEquals(expectedToString, preSignedURL.toString());
    }
}