package io.jenkins.plugins.kobiton.shared.models;

import io.jenkins.plugins.kobiton.shared.utils.PropertyLoader;

public interface ApiVariable {
    String FILE_NAME = PropertyLoader.loadProperty("bodyFileName", "filename");
    String APP_ID = PropertyLoader.loadProperty("bodyAppId", "appId");
    String APP_PATH = PropertyLoader.loadProperty("bodyAppPath", "appPath");
}
