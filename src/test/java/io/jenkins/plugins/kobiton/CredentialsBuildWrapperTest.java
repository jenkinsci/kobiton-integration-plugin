package io.jenkins.plugins.kobiton;

import hudson.model.FreeStyleProject;
import hudson.util.Secret;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.jupiter.api.Assertions.*;

public class CredentialsBuildWrapperTest {
    @Rule
    public JenkinsRule jenkins = new JenkinsRule();
    final String username = "username";
    final Secret apiKey = Secret.fromString("apiKey");
    final String standaloneUrl = "standaloneUrl";

    @Test
    public void setUp_UsernameApiKeyAndStandaloneUrlGiven_ShouldBuildSuccess() throws Exception {
        Credential expected = new Credential(username, apiKey);
        FreeStyleProject project = jenkins.createFreeStyleProject();
        CredentialsBuildWrapper buildWrapper = new CredentialsBuildWrapper(username, apiKey, standaloneUrl);
        project.getBuildWrappersList().add(buildWrapper);

        assertEquals(expected.getCredentials(), buildWrapper.getCredential().getCredentials());
        jenkins.buildAndAssertSuccess(project);
    }
}