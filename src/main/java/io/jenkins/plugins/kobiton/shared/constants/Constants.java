package io.jenkins.plugins.kobiton.shared.constants;

import io.jenkins.plugins.kobiton.shared.utils.PropertyLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Constants {
    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String DEFAULT_BASE_API_URL = PropertyLoader.loadProperty("baseApiUrl", "https://api.kobiton.com/v1");
    public static final String APP_ID_MAPPING = PropertyLoader.loadProperty("appIdSignature", "kobiton-store:{0}");
    public static final String APP_VERSION_ID_MAPPING = PropertyLoader.loadProperty("appVersionIdSignature", "kobiton-store:v{0}");
    public static final class SupportedFile {
        private SupportedFile() {}
        private static final List<String> extensions = PropertyLoader.loadProperties("supportedFileExtensions");
        private static final List<String> mimeTypes = PropertyLoader.loadProperties("supportedMimeTypes");

        public static List<String> getExtensions() {
            return new ArrayList<>(extensions);
        }

        public static List<String> getMimeTypes() {
            return new ArrayList<>(mimeTypes);
        }
    }
}
