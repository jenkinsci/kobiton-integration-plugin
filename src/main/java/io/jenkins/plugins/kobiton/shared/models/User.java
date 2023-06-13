package io.jenkins.plugins.kobiton.shared.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private final int id;
    private final String username;
    private final String email;
    private final Boolean disabled;
    private final Boolean isActivated;

    @JsonCreator
    public User(@JsonProperty("id") int id,
                @JsonProperty("user_name") String username,
                @JsonProperty("email") String email,
                @JsonProperty("disabled") Boolean disabled,
                @JsonProperty("is_activated") Boolean isActivated) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.disabled = disabled;
        this.isActivated = isActivated;
    }

    public int id() { return id; }
    public String username() { return username; }
    public String email() { return email; }
    public Boolean disabled() { return disabled; }
    public Boolean isActivated() { return isActivated; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(id, that.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", user_name='" + username + '\'' +
                ", email='" + email + '\'' +
                "}";
    }
}
