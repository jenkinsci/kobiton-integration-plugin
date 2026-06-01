package io.jenkins.plugins.kobiton.services.user;

import io.jenkins.plugins.kobiton.ApiEndpoint;
import io.jenkins.plugins.kobiton.services.HttpService;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import io.jenkins.plugins.kobiton.shared.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {

    private static final String BASE_URL = "https://i.love.kobiton.very/much";
    private final Credential credential = new Credential("username", "apiKey");
    private final DefaultUserService userService = new DefaultUserService();
    private final HttpClient mockClient = mock(HttpClient.class);
    private final HttpResponse<String> mockResponse = mock(HttpResponse.class);

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        ApiEndpoint.getInstance().setBaseUrl(BASE_URL);

        HttpService httpService = new HttpService.Builder().withHttpClient(mockClient).build();
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);

        String responseBody = "{\"name\": \"Kobiton Boss\", \"disabled\": false}";
        when(mockResponse.body()).thenReturn(responseBody);

        Field privateField = DefaultUserService.class.getDeclaredField("httpService");
        privateField.setAccessible(true);
        privateField.set(userService, httpService);
    }

    @Test
    void getUrl_EndPointGiven_ShouldReturnCorrectUrl() {
        String endpoint = "/users";

        String result = userService.getUrl(endpoint);

        assertEquals(BASE_URL + endpoint, result);
    }

    @Test
    void getUser_ValidUserCredentialGiven_ShouldReturnUser() throws IOException {
        User expectedUser = new User(0, null, null, null, null);

        User user = userService.getUser(credential);

        assertEquals(expectedUser, user);
    }

    @Test
    void isUserDisabled_ValidUserCredentialGiven_ShouldReturnFalse() throws IOException {
        boolean expected = false;

        boolean result = userService.isUserDisabled(credential);

        assertEquals(expected, result);
    }
}