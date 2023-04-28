package io.jenkins.plugins.kobiton.services.app;


public interface AppService {
    String APP_BASE_URL = "/apps";
    String APP_UPLOAD_URL = APP_BASE_URL + "/uploadUrl";
    String GET_APP_VERSION_INFO = "/app/versions/%s";

    String getUrl(String endpoint);
}
