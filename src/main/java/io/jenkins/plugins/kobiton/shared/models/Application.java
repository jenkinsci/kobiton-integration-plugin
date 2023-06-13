package io.jenkins.plugins.kobiton.shared.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jenkins.plugins.kobiton.shared.constants.Constants;

import java.util.Objects;
import java.text.MessageFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    private Integer appId;
    private final Integer versionId;

    @JsonCreator
    public Application(@JsonProperty("appId") Integer appId,
                       @JsonProperty("versionId") Integer versionId) {
        this.appId = appId;
        this.versionId = versionId;
    }

    public Integer appId() { return appId; }
    public Integer versionId() { return versionId; }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(appId, that.appId()) && Objects.equals(versionId, that.versionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, versionId);
    }

    @Override
    public String toString() {
        if (appId == null) {
            return "{versionId='" + versionId + '\'' + "}";
        }

        return "{" +
                "appId='" + appId + '\'' +
                ", versionId='" + versionId + '\'' +
                "}";
    }

    /**
     * get the value of the “app” capability for test script
     *
     * @return appID signature (e.g. "kobiton-store:123" or "kobiton-store:v48" )
     */
    public String getAppOrVersionId() {
        if (appId != null) {
            return MessageFormat.format(Constants.APP_ID_MAPPING, appId.toString());
        }
        return MessageFormat.format(Constants.APP_VERSION_ID_MAPPING, versionId.toString());
    }
}
