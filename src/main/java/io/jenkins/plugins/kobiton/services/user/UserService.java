package io.jenkins.plugins.kobiton.services.user;

import io.jenkins.plugins.kobiton.shared.utils.PropertyLoader;

public interface UserService {
    String USER_BASE_URL = PropertyLoader.loadProperty("usersEndpoint", "/users");
    String CURRENT_USER_URL = PropertyLoader.loadProperty("currentUserEndpoint", USER_BASE_URL + "/me");
    String getUrl(String endpoint);
}
