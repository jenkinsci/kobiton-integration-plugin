package io.jenkins.plugins.kobiton.services.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jenkins.plugins.kobiton.ApiEndpoint;
import io.jenkins.plugins.kobiton.services.HttpService;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import io.jenkins.plugins.kobiton.shared.models.User;

import java.io.IOException;

public class DefaultUserService implements UserService {
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
     * Map response from API to User object
     */
    private static User toUser(String response) throws IOException {
        return OBJECT_MAPPER.readValue(response, User.class);
    }

    /**
     * Get user info from credential
     *
     * @param credential credential
     * @return User object
     * @throws IOException IOException
     */
    public User getUser(Credential credential) throws IOException {
        String response = httpService.get(getUrl(CURRENT_USER_URL), credential);
        return toUser(response);
    }

    /**
     * Check if user exists based on credential
     *
     * @param credential credential
     * @return boolean true if user exists
     * @throws IOException IOException
     */
    public boolean isUserDisabled(Credential credential) throws IOException {
        User user = getUser(credential);
        return user.disabled();
    }
}
