package io.jenkins.plugins.kobiton.services;

import io.jenkins.plugins.kobiton.services.file.DefaultFileOperations;
import io.jenkins.plugins.kobiton.services.file.FileOperations;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import io.jenkins.plugins.kobiton.shared.utils.HttpUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.NoSuchFileException;

public class HttpService {
    private final HttpClient client;
    private final FileOperations fileOperations;

    private HttpService(HttpClient client, FileOperations fileOperations) {
        this.client = client;
        this.fileOperations = fileOperations;
    }

    public static class Builder {
        private HttpClient client;
        private FileOperations fileOperations;

        public Builder withHttpClient(HttpClient client) {
            this.client = client;
            return this;
        }

        public Builder withFileOperations(FileOperations fileOperations) {
            this.fileOperations = fileOperations;
            return this;
        }

        public HttpService build() {
            if (client == null) {
                client = HttpClient.newHttpClient();
            }
            if (fileOperations == null) {
                fileOperations = new DefaultFileOperations();
            }
            return new HttpService(client, fileOperations);
        }
    }

    /**
     * Send HTTP request
     *
     * @param request HttpRequest
     * @return response body
     * @throws IOException if API call fail
     */
    private String sendRequest(HttpRequest request) throws IOException {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode == 200) {
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
     * Call GET request with credential
     *
     * @param url API url
     * @param credential credential
     * @return response body
     * @throws IOException if API call fail
     */
    public String get(String url, Credential credential) throws IOException {
        HttpRequest request = HttpUtils.createGetRequest(url, credential);
        return sendRequest(request);
    }

    /**
     * Call POST request with credential and body
     *
     * @param url API url
     * @param credential credential
     * @param body request body
     * @return response body
     * @throws IOException if API call fail
     */
    public String post(String url, Credential credential, String body) throws IOException {
        HttpRequest request = HttpUtils.createPostRequest(url, credential, body);
        return sendRequest(request);
    }

    /**
     * Call PUT request with file content
     *
     * @param url API url
     * @param filePath path to upload file
     * @return response body
     * @throws IOException if API call fail
     */
    public String put(String url, String filePath) throws IOException {
        byte[] fileContent;

        try {
            fileContent = fileOperations.readFileContent(filePath);
        } catch (NoSuchFileException e) {
            throw new IOException("File not found", e);
        }

        HttpRequest request = HttpUtils.createPutRequest(url, fileContent);

        return sendRequest(request);
    }
}
