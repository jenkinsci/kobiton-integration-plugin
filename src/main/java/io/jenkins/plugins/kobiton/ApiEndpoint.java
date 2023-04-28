package io.jenkins.plugins.kobiton;

import io.jenkins.plugins.kobiton.shared.constants.Constants;
import io.jenkins.plugins.kobiton.shared.utils.StringUtils;

public class ApiEndpoint {
    private static ApiEndpoint instance;
    private String baseUrl;

    private ApiEndpoint() {
    }

    public static ApiEndpoint getInstance() {
        if (instance == null) {
            instance = new ApiEndpoint();
        }
        return instance;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Set base URL for API calls
     *
     * @param standaloneUrl standalone URL if none, use Kobiton Production URL
     */
    public void setBaseUrl(String standaloneUrl) {
        this.baseUrl = StringUtils.isNullOrEmpty(standaloneUrl) ? Constants.DEFAULT_BASE_API_URL : standaloneUrl;
    }
}
