package io.jenkins.plugins.kobiton.services.app;

import io.jenkins.plugins.kobiton.shared.utils.PropertyLoader;

public interface AppService {
    String APP_BASE_URL = PropertyLoader.loadProperty("appsEndpoint", "/apps");
    String APP_UPLOAD_URL = PropertyLoader.loadProperty("appUploadEndpoint", APP_BASE_URL + "/uploadUrl");
    String getUrl(String endpoint);
}
