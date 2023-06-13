package io.jenkins.plugins.kobiton.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class MappingUtils {
    private MappingUtils() { throw new IllegalStateException("Utils class"); }

    /**
     * Data binding the response string from API into a specific object type
     *
     * @param response response string
     * @param objectType object type
     * @return object
     * @param <T> object type
     * @throws IOException IOException
     */
    public static <T> T mapResponseToObject(String response, Class<T> objectType, ObjectMapper objectMapper)
            throws IOException {
        return objectMapper.readValue(response, objectType);
    }
}
