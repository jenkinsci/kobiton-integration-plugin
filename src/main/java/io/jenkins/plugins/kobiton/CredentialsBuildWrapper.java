package io.jenkins.plugins.kobiton;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.util.Secret;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import io.jenkins.plugins.kobiton.shared.models.EnvironmentVar;
import jenkins.tasks.SimpleBuildWrapper;
import org.kohsuke.stapler.DataBoundConstructor;

public class CredentialsBuildWrapper extends SimpleBuildWrapper {
    private final Credential credential;
    private final String standaloneUrl;

    @DataBoundConstructor
    public CredentialsBuildWrapper(String username, Secret apiKey, String standaloneUrl) {
        credential = new Credential(username, apiKey);
        this.standaloneUrl = standaloneUrl;
    }

    public Credential getCredential() {
        return credential;
    }

    public String getUsername() {
        return credential.getUsername();
    }

    public String getApiKey() {
        return credential.getApiKey();
    }

    public String getStandaloneUrl() {
        return standaloneUrl;
    }

    @Override
    public void setUp(Context context,
                      Run<?, ?> build,
                      FilePath workspace,
                      Launcher launcher,
                      TaskListener listener,
                      EnvVars initialEnvironment) {
        context.env(EnvironmentVar.USERNAME, getUsername());
        context.env(EnvironmentVar.API_KEY, getApiKey());
        ApiEndpoint.getInstance().setBaseUrl(getStandaloneUrl());
    }
}
