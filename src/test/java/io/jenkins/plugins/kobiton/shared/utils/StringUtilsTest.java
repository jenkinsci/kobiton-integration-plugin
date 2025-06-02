package io.jenkins.plugins.kobiton.shared.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    void StringUtils_Constructor_ShouldBePrivate() throws NoSuchMethodException {
        Constructor<StringUtils> constructor = StringUtils.class.getDeclaredConstructor();
        boolean expected = true;

        assertEquals(expected, Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void isNullOrEmpty_NullStringGiven_ShouldReturnsTrue() {
        boolean expected = true;

        assertEquals(expected, StringUtils.isNullOrEmpty(null));
    }

    @Test
    void isNullOrEmpty_EmptyStringGive_ShouldReturnsTrue() {
        boolean expected = true;

        assertEquals(expected, StringUtils.isNullOrEmpty(""));
    }

    @Test
    void isNullOrEmpty_StringWithSpaceGiven_ShouldReturnsFalse() {
        boolean expected = false;

        assertEquals(expected, StringUtils.isNullOrEmpty(" "));
    }

    @Test
    void isNullOrEmpty_NonEmptyStringGiven_ShouldReturnsFalse() {
        boolean expected = false;

        assertEquals(expected, StringUtils.isNullOrEmpty("i_love_kobiton"));
    }
}