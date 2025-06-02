package io.jenkins.plugins.kobiton.shared.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    void stringUtils_Constructor_ShouldBePrivate() throws NoSuchMethodException {
        Constructor<StringUtils> constructor = StringUtils.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void isNullOrEmpty_NullStringGiven_ShouldReturnsTrue() {
        assertTrue(StringUtils.isNullOrEmpty(null));
    }

    @Test
    void isNullOrEmpty_EmptyStringGive_ShouldReturnsTrue() {
        assertTrue(StringUtils.isNullOrEmpty(""));
    }

    @Test
    void isNullOrEmpty_StringWithSpaceGiven_ShouldReturnsFalse() {
        assertFalse(StringUtils.isNullOrEmpty(" "));
    }

    @Test
    void isNullOrEmpty_NonEmptyStringGiven_ShouldReturnsFalse() {
        assertFalse(StringUtils.isNullOrEmpty("i_love_kobiton"));
    }
}