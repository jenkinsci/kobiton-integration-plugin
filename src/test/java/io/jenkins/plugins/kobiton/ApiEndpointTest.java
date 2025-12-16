package io.jenkins.plugins.kobiton;

import io.jenkins.plugins.kobiton.shared.constants.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiEndpointTest {

    private ApiEndpoint apiEndpoint;

    @BeforeEach
    void setUp() {
        apiEndpoint = ApiEndpoint.getInstance();
    }

    @Test
    void getInstance_InstanceGiven_ShouldReturnSameInstance() {
        ApiEndpoint secondInstance = ApiEndpoint.getInstance();

        assertEquals(apiEndpoint, secondInstance);
    }

    @Test
    void getBaseUrl_NoSetterGiven_ShouldReturnDefaultUrl() {
        String defaultBaseUrl = apiEndpoint.getBaseUrl();

        assertEquals(Constants.DEFAULT_BASE_API_URL, defaultBaseUrl);
    }

    @Test
    void getBaseUrl_SetterGiven_ShouldReturnUpdatedUrl() {
        String newBaseUrl = "https://api.example.com";
        apiEndpoint.setBaseUrl(newBaseUrl);

        String updatedBaseUrl = apiEndpoint.getBaseUrl();

        assertEquals(newBaseUrl, updatedBaseUrl);
    }

    @Test
    void setBaseUrl_NullGiven_ShouldSetDefaultUrl() {
        apiEndpoint.setBaseUrl(null);
        String updatedBaseUrl = apiEndpoint.getBaseUrl();

        assertEquals(Constants.DEFAULT_BASE_API_URL, updatedBaseUrl);
    }

    @Test
    void setBaseUrl_EmptyStringGiven_ShouldSetDefaultUrl() {
        apiEndpoint.setBaseUrl("");
        String updatedBaseUrl = apiEndpoint.getBaseUrl();

        assertEquals(Constants.DEFAULT_BASE_API_URL, updatedBaseUrl);
    }
}