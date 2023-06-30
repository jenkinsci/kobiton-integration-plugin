package io.jenkins.plugins.kobiton.shared.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyLoaderTest {
    @Test
    void PropertyLoader_Constructor_ShouldBePrivate() throws NoSuchMethodException {
        Constructor<PropertyLoader> constructor = PropertyLoader.class.getDeclaredConstructor();
        boolean expected = true;

        assertEquals(expected, Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void loadProperty_DefaultValueGiven_ShouldReturnDefaultValue() {
        String key = "someKey";
        String defaultValue = "defaultValue";

        String actualValue = PropertyLoader.loadProperty(key, defaultValue);

        assertEquals("defaultValue", actualValue);
    }

    @Test
    void loadProperties_WrongKeyGiven_ShouldReturnEmptyList() {
        String key = "someKey";
        List<String> expectedProperties = List.of();

        List<String> actualProperties = PropertyLoader.loadProperties(key, new String[]{});

        assertEquals(expectedProperties, actualProperties);
    }
}