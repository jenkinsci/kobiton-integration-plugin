package io.jenkins.plugins.kobiton.services.app;

import io.jenkins.plugins.kobiton.ApiEndpoint;
import io.jenkins.plugins.kobiton.services.HttpService;
import io.jenkins.plugins.kobiton.services.file.FileOperations;
import io.jenkins.plugins.kobiton.shared.models.Application;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import io.jenkins.plugins.kobiton.shared.models.PreSignedURL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@RunWith(MockitoJUnitRunner.class)
class AppUploaderServiceTest {
    private static final String BASE_URL = "https://i.love.kobiton.very/much";
    private final Credential credential = new Credential("username", "apiKey");
    private final AppUploaderService service = new AppUploaderService();
    private final HttpClient mockClient = mock(HttpClient.class);
    private final HttpResponse<String> mockResponse = mock(HttpResponse.class);

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        ApiEndpoint.getInstance().setBaseUrl(BASE_URL);

        HttpService httpService = new HttpService.Builder().withHttpClient(mockClient).build();
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);

        String responseBody = "{\"appPath\": \"appPath\", \"url\": \"url\"}";
        when(mockResponse.body()).thenReturn(responseBody);

        Field privateField = AppUploaderService.class.getDeclaredField("httpService");
        privateField.setAccessible(true);
        privateField.set(service, httpService);
    }

    @Test
    void getUrl_EndPointGiven_ShouldReturnCorrectUrl() {
        String ENDPOINT = "/apps";

        String result = service.getUrl(ENDPOINT);

        assertEquals(BASE_URL + ENDPOINT, result);
    }

    @Test
    void generatePreSignedUploadURL_CredentialsFileNameAndAppIdGiven_ShouldReturnPreSignedURL() throws IOException {
        PreSignedURL expected = new PreSignedURL("appPath", "url");

        PreSignedURL preSignedURL = service.generatePreSignedUploadURL(credential, "test.txt", 123);

        assertEquals(expected, preSignedURL);
    }

    @Test
    void generatePreSignedUploadURL_CredentialsAndFileNameGiven_ShouldReturnPreSignedURL() throws IOException {
        PreSignedURL expected = new PreSignedURL("appPath", "url");

        PreSignedURL preSignedURL = service.generatePreSignedUploadURL(credential, "test.txt", null);

        assertEquals(expected, preSignedURL);
    }

    @Test
    void uploadFileToS3_ValidUrlAndUploadPathGiven_ShouldNotThrowError() throws IOException, NoSuchFieldException, IllegalAccessException {
        String fileContent = "File content";

        FileOperations mockFileOperations = mock(FileOperations.class);
        when(mockFileOperations.readFileContent(any(String.class))).thenReturn(fileContent.getBytes());

        HttpService httpService = new HttpService.Builder().withHttpClient(mockClient).withFileOperations(mockFileOperations).build();
        Field privateField = AppUploaderService.class.getDeclaredField("httpService");
        privateField.setAccessible(true);
        privateField.set(service, httpService);

        assertDoesNotThrow(() -> {
            service.uploadFileToS3(BASE_URL, "path/to/app");
        });
    }

    @Test
    void createApplication_ValidAppIdAndVersionIdGiven_ShouldReturnApplication() throws IOException {
        Application expected = new Application(123, 456);
        String responseBody = "{\"appId\":123,\"versionId\":456}";
        when(mockResponse.body()).thenReturn(responseBody);

        Application app = service.createApplication(credential, "fileName", "path/to/app");

        assertEquals(expected, app);
    }
}