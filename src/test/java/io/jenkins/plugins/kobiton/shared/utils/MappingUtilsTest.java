package io.jenkins.plugins.kobiton.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jenkins.plugins.kobiton.shared.models.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class MappingUtilsTest {

    @Test
    void mappingUtils_Constructor_ShouldBePrivate() throws NoSuchMethodException {
        Constructor<MappingUtils> constructor = MappingUtils.class.getDeclaredConstructor();
        boolean expected = true;

        assertEquals(expected, Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void mapResponseToObject_testMapResponseToObject() throws IOException {
        String response = "{\"id\": 1}";
        Class<User> objectType = User.class;
        ObjectMapper objectMapper = new ObjectMapper();

        User user = MappingUtils.mapResponseToObject(response, objectType, objectMapper);

        assertNotNull(user);
        assertEquals(1, user.id());
    }
}