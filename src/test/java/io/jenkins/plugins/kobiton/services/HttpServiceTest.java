package io.jenkins.plugins.kobiton.services;

import io.jenkins.plugins.kobiton.services.file.FileOperations;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class HttpServiceTest {

    private static final String URL = "https://i.love.kobiton.very/much";
    private final Credential credential = new Credential("username", "apiKey");
    private HttpService httpService;
    private final HttpClient mockClient = mock(HttpClient.class);
    private final HttpResponse<String> mockResponse = mock(HttpResponse.class);
    private final String responseBody = "Response body";
    private final String invalidFilePath = "i-am-not-a-file";

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        httpService = new HttpService.Builder().withHttpClient(mockClient).build();
    }

    @Test
    void builder_NoHttpClientAndFileOperationsGiven_ShouldReturnBuild() {
        httpService = new HttpService.Builder().build();
        assertNotNull(httpService);
    }

    @Test
    void sendRequest_Successful200RequestGiven_ShouldReturnResponseBody() throws IOException {
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(responseBody);

        String response = httpService.get(URL, credential);

        assertEquals(responseBody, response);
    }

    @Test
    void sendRequest_UnauthorizedRequestGiven_ShouldThrowIOException() {
        when(mockResponse.statusCode()).thenReturn(404);
        when(mockResponse.body()).thenReturn(responseBody);

        assertThrows(IOException.class, () -> httpService.get(URL, credential));
    }

    @Test
    void sendRequest_FailedRequestGiven_ShouldThrowIOException() {
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockResponse.body()).thenReturn(responseBody);

        assertThrows(IOException.class,  () -> httpService.get(URL, credential));
    }

    @Test
    void post_SuccessfulRequestGiven_ShouldReturnResponseBody() throws IOException {
        String requestBody = "Request body";
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(responseBody);

        String response = httpService.post(URL, credential, requestBody);

        assertEquals(responseBody, response);
    }

    @Test
    void put_InvalidFilePathGiven_ShouldThrowIOException() {
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(responseBody);

        assertThrows(IOException.class, () -> httpService.put(URL, invalidFilePath));
    }

    @Test
    void put_SuccessfulRequestGiven_ShouldReturnResponseBody() throws IOException {
        String validFilePath = "path/to/file.txt";
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(responseBody);

        FileOperations mockFileOperations = mock(FileOperations.class);
        String fileContent = "File content";
        when(mockFileOperations.readFileContent(any(String.class))).thenReturn(fileContent.getBytes());
        httpService = new HttpService.Builder().withHttpClient(mockClient).withFileOperations(mockFileOperations).build();

        String response = httpService.put(URL, validFilePath);

        assertEquals(responseBody, response);
    }
}