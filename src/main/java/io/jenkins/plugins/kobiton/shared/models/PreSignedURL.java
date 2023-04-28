package io.jenkins.plugins.kobiton.shared.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PreSignedURL {
    private final String appPath;  // used to create application or application version
    private final String url;   // used to upload file to S3

    @JsonCreator
    public PreSignedURL(@JsonProperty("appPath") String appPath,
                        @JsonProperty("url") String url) {
        this.appPath = appPath;
        this.url = url;
    }

    public String appPath() {
        return appPath;
    }

    public String url() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreSignedURL that = (PreSignedURL) o;
        return Objects.equals(appPath, that.appPath()) && Objects.equals(url, that.url());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appPath, url);
    }

    @Override
    public String toString() {
        return "{" +
                "appPath='" + appPath + '\'' +
                ", url='" + url + '\'' +
                "}";
    }
}
