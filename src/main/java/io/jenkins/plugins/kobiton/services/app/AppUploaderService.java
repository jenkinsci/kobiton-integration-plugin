package io.jenkins.plugins.kobiton.services.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jenkins.plugins.kobiton.ApiEndpoint;
import io.jenkins.plugins.kobiton.services.HttpService;
import io.jenkins.plugins.kobiton.shared.models.*;
import io.jenkins.plugins.kobiton.shared.utils.MappingUtils;

import java.io.IOException;

public class AppUploaderService implements AppService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final HttpService httpService = new HttpService.Builder().build();

    /**
     * Get URL for API call
     */
    @Override
    public String getUrl(String endpoint) {
        return ApiEndpoint.getInstance().getBaseUrl() + endpoint;
    }

    /**
     * Create a pre-signed URL for uploading file to S3
     *
     * @param credential credential
     * @param fileName file name
     * @return PreSignedURL object {
     * appPath: path for application or application version creation
     * url: pre-signed URL for file uploading
     * }
     * @throws IOException IOException
     */
    public PreSignedURL generatePreSignedUploadURL(Credential credential, String fileName, Integer appId) throws IOException {
        ObjectNode requestBodyObject = OBJECT_MAPPER.createObjectNode();
        requestBodyObject.put(ApiVariable.FILE_NAME, fileName);

        if (appId != null) {
            requestBodyObject.put(ApiVariable.APP_ID, appId);
        }

        String requestBody = OBJECT_MAPPER.writeValueAsString(requestBodyObject);
        String response = httpService.post(getUrl(APP_UPLOAD_URL), credential, requestBody);

        return MappingUtils.mapResponseToObject(response, PreSignedURL.class, OBJECT_MAPPER);
    }

    /**
     * Upload binary file to the pre-signed URL that has been created
     *
     * @param preSignedUrl pre-signed URL for uploading file to S3
     * @param uploadPath path to file
     * @return true if no error was thrown
     * @throws IOException IOException
     */
    public Boolean uploadFileToS3(String preSignedUrl, String uploadPath) throws IOException {
        httpService.put(preSignedUrl, uploadPath);
        return true;
    }

    /**
     * Create an application or an application version from the file that has been uploaded to S3
     *
     * @param credential credential
     * @param fileName file name
     * @param appPath path returned when created pre-signed URL
     * @return Application object { appId, versionId }
     * @throws IOException IOException
     */
    public Application createApplication(Credential credential, String fileName, String appPath) throws IOException {
        ObjectNode requestBodyObject = OBJECT_MAPPER.createObjectNode();
        requestBodyObject.put(ApiVariable.FILE_NAME, fileName);
        requestBodyObject.put(ApiVariable.APP_PATH, appPath);
        String requestBody = OBJECT_MAPPER.writeValueAsString(requestBodyObject);

        String response = httpService.post(getUrl(APP_BASE_URL), credential, requestBody);

        return MappingUtils.mapResponseToObject(response, Application.class, OBJECT_MAPPER);
    }
}
