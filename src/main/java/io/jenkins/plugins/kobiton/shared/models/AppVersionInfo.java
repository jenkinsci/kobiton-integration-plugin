package io.jenkins.plugins.kobiton.shared.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppVersionInfo {
    private final Integer appId;
    private final String state;

    @JsonCreator
    public AppVersionInfo(@JsonProperty("appId") Integer appId,
                       @JsonProperty("state") String state) {
        this.appId = appId;
        this.state = state;
    }

    public Integer appId() { return appId; }
    public String state() { return state; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppVersionInfo that = (AppVersionInfo) o;
        return Objects.equals(appId, that.appId()) && Objects.equals(state, that.state());
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + appId;
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "appId='" + appId + '\'' +
                ", state='" + state + '\'' +
                "}";
    }
}
