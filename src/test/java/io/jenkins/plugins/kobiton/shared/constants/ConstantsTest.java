package io.jenkins.plugins.kobiton.shared.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void constants_Constructor_IsPrivate() throws NoSuchMethodException {
        Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        boolean expected = true;

        assertEquals(expected, Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void getExtensions_ShouldReturnCorrectly() {
        List<String> expectedExtensions = Arrays.asList("apk", "ipa");
        List<String> extensions = Constants.SupportedFile.getExtensions();

        assertEquals(expectedExtensions, extensions);
        assertNotSame(expectedExtensions, extensions);
    }

    @Test
    void getMimeTypes_ShouldReturnCorrectly() {
        List<String> expectedMimeTypes = Arrays.asList(
                "application/vnd.android.package-archive",
                "application/octet-stream"
        );
        List<String> mimeTypes = Constants.SupportedFile.getMimeTypes();

        assertEquals(expectedMimeTypes, mimeTypes);
        assertNotSame(expectedMimeTypes, mimeTypes);
    }
}