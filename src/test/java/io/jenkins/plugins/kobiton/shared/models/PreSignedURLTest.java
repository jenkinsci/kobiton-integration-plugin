package io.jenkins.plugins.kobiton.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PreSignedURLTest {

    private static final String APP_PATH = "/path/to/app";
    private static final String URL = "https://i.love.kobiton/very/much";

    @Test
    void constructorAndGetters_ShouldReturnCorrectly() {
        PreSignedURL preSignedURL = new PreSignedURL(APP_PATH, URL);

        assertEquals(APP_PATH, preSignedURL.appPath());
        assertEquals(URL, preSignedURL.url());
    }

    @Test
    void equalsAndHashCode_ShouldReturnCorrectly() {
        PreSignedURL url1 = new PreSignedURL(APP_PATH, URL);
        PreSignedURL url2 = new PreSignedURL(APP_PATH, URL);
        PreSignedURL url3 = new PreSignedURL("/other/path", URL);
        PreSignedURL url4 = new PreSignedURL(APP_PATH, "this-url-is-weird");
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
        PreSignedURL preSignedURL = new PreSignedURL(APP_PATH, URL);
        String expectedToString = "{appPath='/path/to/app', url='https://i.love.kobiton/very/much'}";

        assertEquals(expectedToString, preSignedURL.toString());
    }
}