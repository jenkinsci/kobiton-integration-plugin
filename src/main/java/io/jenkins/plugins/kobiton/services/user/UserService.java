package io.jenkins.plugins.kobiton.services.user;

public interface UserService {
    String USER_BASE_URL = "/users";
    String CURRENT_USER_URL = USER_BASE_URL + "/me";
    String getUrl(String endpoint);
}
