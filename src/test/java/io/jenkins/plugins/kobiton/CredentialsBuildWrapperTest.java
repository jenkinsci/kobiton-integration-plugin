package io.jenkins.plugins.kobiton;

import hudson.model.FreeStyleProject;
import hudson.util.Secret;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithJenkins
class CredentialsBuildWrapperTest {

    private static final String USERNAME = "username";
    private static final Secret API_KEY = Secret.fromString("apiKey");
    private static final String STANDALONE_URL = "standaloneUrl";

    private JenkinsRule jenkins;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        jenkins = rule;
    }

    @Test
    void setUp_UsernameApiKeyAndStandaloneUrlGiven_ShouldBuildSuccess() throws Exception {
        Credential expected = new Credential(USERNAME, API_KEY);
        FreeStyleProject project = jenkins.createFreeStyleProject();
        CredentialsBuildWrapper buildWrapper = new CredentialsBuildWrapper(USERNAME, API_KEY,
            STANDALONE_URL);
        project.getBuildWrappersList().add(buildWrapper);

        assertEquals(expected.getCredentials(), buildWrapper.getCredential().getCredentials());
        jenkins.buildAndAssertSuccess(project);
    }
}