package io.jenkins.plugins.kobiton.shared.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class Constants {
    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String DEFAULT_BASE_API_URL = "https://api.kobiton.com/v1";
    public static final String APP_ID_MAPPING = "kobiton-store:";
    public static final String APP_VERSION_ID_MAPPING = "kobiton-store:v";
    public static final class SupportedFile {
        private SupportedFile() {}
        private static final List<String> extensions = Arrays.asList("apk", "ipa");
        private static final List<String> mimeTypes = Arrays.asList(
                "application/vnd.android.package-archive",
                "application/octet-stream");

        public static List<String> getExtensions() {
            return new ArrayList<>(extensions);
        }

        public static List<String> getMimeTypes() {
            return new ArrayList<>(mimeTypes);
        }
    }


}
