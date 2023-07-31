package io.jenkins.plugins.kobiton.shared.utils;

import io.jenkins.plugins.kobiton.shared.models.Credential;

import java.net.URI;
import java.net.http.HttpRequest;

/**
 * Utility for HTTP request creating, authorizing, etc.
 */
public final class HttpUtils {
    private HttpUtils() { throw new IllegalStateException("Utils class"); }

    /**
     * Add authorization to HTTP header
     *
     * @param url API url
     * @param credential credential
     * @return HttpRequest Builder with Authorization header
     */
    public static HttpRequest.Builder createAuthHeader(String url, Credential credential) {
        return HttpRequest
                .newBuilder(URI.create(url))
                .header("Authorization", "Basic " + CredentialUtils.encodeCredentials(credential));
    }

    /**
     * Create GET HttpRequest
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
     * Create POST HttpRequest
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
     * Create PUT HttpRequest
     *
     * @param url pre-signed url for S3
     * @param fileContent file content
     * @return PUT HttpRequest
     */
    public static HttpRequest createPutRequest(String url, byte[] fileContent) {
        return HttpRequest.newBuilder(URI.create(url))
                .header("x-amz-tagging", "unsaved=true")
                .header("Content-Type", "application/octet-stream")
                .PUT(HttpRequest.BodyPublishers.ofByteArray(fileContent))
                .build();
    }
}
