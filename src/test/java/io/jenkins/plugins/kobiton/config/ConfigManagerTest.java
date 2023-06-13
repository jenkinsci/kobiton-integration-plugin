package io.jenkins.plugins.kobiton.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ConfigManagerTest {
    private static final String PROPERTIES_CONTENT = "key1=value1\nkey2=value2\nmyArray=value1,value2,value3";
    private ConfigManager configManager;

    @BeforeEach
    public void setUp() {
        Properties properties = new Properties();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(PROPERTIES_CONTENT.getBytes(StandardCharsets.UTF_8));
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configManager = new ConfigManager(properties);
    }

    @Test
    void getProperty_KeyGiven_ShouldReturnValueCorrectly() {
        String value1 = configManager.getProperty("key1", "default");
        String value2 = configManager.getProperty("key2", "default");
        String defaultValue = configManager.getProperty("nonexistent", "default");

        assertEquals("value1", value1);
        assertEquals("value2", value2);
        assertEquals("default", defaultValue);
    }

    @Test
    void getArrayProperty_KeyGiven__ShouldReturnValueCorrectly() {
        List<String> myArray = configManager.getArrayProperty("myArray");
        List<String> nonexistentArray = configManager.getArrayProperty("nonexistent");

        assertEquals(3, myArray.size());
        assertEquals("value1", myArray.get(0));
        assertEquals("value2", myArray.get(1));
        assertEquals("value3", myArray.get(2));
        assertEquals(0, nonexistentArray.size());
    }

    @Test
    void Builder_ConfigFileGiven_ShouldBuildSuccessfully() {
        assertDoesNotThrow(() -> {
            configManager = new ConfigManager.Builder().filePath("config.properties").build();
        });
        String defaultValue = configManager.getProperty("nonexistent", "default");

        assertEquals("default", defaultValue);
    }

    @Test
    void Builder_WrongConfigFileGiven_ShouldNotThrowError() {
        assertDoesNotThrow(() -> {
            configManager = new ConfigManager.Builder().filePath("fake").build();
        });
    }
}