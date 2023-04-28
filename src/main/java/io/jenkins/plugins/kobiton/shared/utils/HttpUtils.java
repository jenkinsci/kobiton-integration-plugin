package io.jenkins.plugins.kobiton.shared.utils;

import io.jenkins.plugins.kobiton.shared.models.Credential;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class HttpUtils {
    private HttpUtils() { throw new IllegalStateException("Utils class"); }
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    /**
     * Add authorization to HTTP header
     *
     * @param url API url
     * @param credential credential
     * @return HttpRequest Builder with Authorization header: Basic <credentials>
     */
    public static HttpRequest.Builder createAuthHeader(String url, Credential credential) {
        return HttpRequest
                .newBuilder(URI.create(url))
                .header("Authorization", "Basic " + CredentialUtils.encodeCredentials(credential));
    }

    /**
     *
     * @param url API url
     * @param credential credential
     * @return GET HttpRequest
     */
    public static HttpRequest createGetRequest(String url, Credential credential) {
        return createAuthHeader(url, credential)
                .GET()
                .build();
    }

    /**
     *
     * @param url API url
     * @param credential credential
     * @param body request body
     * @return POST HttpRequest
     */
    public static HttpRequest createPostRequest(String url, Credential credential, String body) {
        return createAuthHeader(url, credential)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    /**
     *
     * @param preSignedUrl pre-signed url for S3
     * @param fileContent file content
     * @return PUT HttpRequest
     */
    public static HttpRequest createPutRequest(String preSignedUrl, byte[] fileContent) {
        return HttpRequest.newBuilder(URI.create(preSignedUrl))
                .header("x-amz-tagging", "unsaved=true")
                .header("Content-Type", "application/octet-stream")
                .PUT(HttpRequest.BodyPublishers.ofByteArray(fileContent))
                .build();
    }

    /**
     *
     * @param request HttpRequest
     * @return response body
     * @throws IOException if API call fail
     */
    private static String sendRequest(HttpRequest request) throws IOException {
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode >= 200 && statusCode < 300) {
                return response.body();
            } else if (statusCode == 404) {
                throw new IOException("Unauthorized API");
            } else {
                throw new IOException("API call fail with status code: " + statusCode);
            }

        } catch (IOException | InterruptedException e){
            Thread.currentThread().interrupt();
            throw new IOException("Unable to call API", e);
        }
    }

    /**
     * Call GET request with credential to url
     *
     * @param url API url
     * @param credential credential
     * @return response body
     * @throws IOException if API call fail
     */
    public static String get(String url, Credential credential) throws IOException {
        HttpRequest request = createGetRequest(url, credential);
        return sendRequest(request);
    }

    /**
     * Call POST request with credential and body to url
     *
     * @param url API url
     * @param credential credential
     * @param body request body
     * @return response body
     * @throws IOException if API call fail
     */
    public static String post(String url, Credential credential, String body) throws IOException {
        HttpRequest request = createPostRequest(url, credential, body);
        return sendRequest(request);
    }

    /**
     * Call PUT request with file content to url
     *
     * @param url API url
     * @param filePath path to upload file
     * @return response body
     * @throws IOException if API call fail
     */
    public static String put(String url, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileContent = Files.readAllBytes(path);

        HttpRequest request = createPutRequest(url, fileContent);

        return sendRequest(request);
    }
}
