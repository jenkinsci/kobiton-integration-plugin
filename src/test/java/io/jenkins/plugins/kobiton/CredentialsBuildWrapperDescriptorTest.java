package io.jenkins.plugins.kobiton;

import hudson.model.Descriptor;
import hudson.model.FreeStyleProject;
import io.jenkins.plugins.kobiton.services.user.DefaultUserService;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import org.kohsuke.stapler.StaplerRequest2;

import java.io.IOException;
import java.lang.reflect.Field;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
@WithJenkins
class CredentialsBuildWrapperDescriptorTest {

    private static final String USERNAME = "username";
    private static final String API_KEY = "apiKey";
    private static final String STANDALONE_URL = "standaloneUrl";

    private JenkinsRule jenkinsRule;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        jenkinsRule = rule;
    }


    @Test
    void newInstance_FormDataGiven_ShouldReturnBuildWrapper() throws Descriptor.FormException {
        StaplerRequest2 request = mock(StaplerRequest2.class);
        JSONObject formData = mock(JSONObject.class);
        when(formData.getString("username")).thenReturn(USERNAME);
        when(formData.getString("apiKey")).thenReturn(API_KEY);
        when(formData.getString("standaloneUrl")).thenReturn(STANDALONE_URL);

        Jenkins jenkins = jenkinsRule.getInstance();
        CredentialsBuildWrapperDescriptor descriptor = jenkins.getDescriptorByType(CredentialsBuildWrapperDescriptor.class);

        CredentialsBuildWrapper newInstance = descriptor.newInstance(request, formData);

        assertEquals(USERNAME, newInstance.getUsername());
        assertEquals(API_KEY, newInstance.getApiKey());
        assertEquals(STANDALONE_URL, newInstance.getStandaloneUrl());
    }

    @Test
    void buildWrapperDescriptor_ShouldReturnCorrectly() throws IOException {
        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        Jenkins jenkins = jenkinsRule.getInstance();
        CredentialsBuildWrapperDescriptor descriptor = jenkins.getDescriptorByType(CredentialsBuildWrapperDescriptor.class);

        // getters, setters
        assertNotEquals("", descriptor.getDisplayName());
        assertTrue(descriptor.isApplicable(project));

        // doCheckUsername
        assertEquals("Username cannot be empty", descriptor.doCheckUsername("").getMessage());
        assertNull(descriptor.doCheckUsername(USERNAME).getMessage());

        // doCheckApiKey
        assertEquals("API key cannot be empty", descriptor.doCheckApiKey("").getMessage());
        assertNull(descriptor.doCheckApiKey(API_KEY).getMessage());
    }

    @Test
    void doAuthenticateUser_UsernameApiKeyAndUrlGiven_ShouldReturnMessageCorrectly() throws IOException, NoSuchFieldException, IllegalAccessException {
        DefaultUserService userServiceMock = mock(DefaultUserService.class);
        when(userServiceMock.isUserDisabled(any())).thenReturn(true);

        Jenkins jenkins = jenkinsRule.getInstance();
        CredentialsBuildWrapperDescriptor descriptor = jenkins.getDescriptorByType(CredentialsBuildWrapperDescriptor.class);

        Field userServiceField = CredentialsBuildWrapperDescriptor.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(descriptor, userServiceMock);

        assertEquals("Username and API key cannot be empty", descriptor.doAuthenticateUser(null, null,
            STANDALONE_URL).getMessage());
        assertEquals("Username and API key cannot be empty", descriptor.doAuthenticateUser(USERNAME, null,
            STANDALONE_URL).getMessage());
        assertEquals("Username and API key cannot be empty", descriptor.doAuthenticateUser(null,
            API_KEY, STANDALONE_URL).getMessage());
        assertEquals("Unauthorized", descriptor.doAuthenticateUser(USERNAME, API_KEY,
            STANDALONE_URL).getMessage());

        when(userServiceMock.isUserDisabled(any())).thenReturn(false);

        assertEquals("Success", descriptor.doAuthenticateUser(USERNAME, API_KEY, STANDALONE_URL).getMessage());

        when(userServiceMock.isUserDisabled(any())).thenThrow(IOException.class);

        assertEquals("Unauthorized", descriptor.doAuthenticateUser(USERNAME, API_KEY,
            STANDALONE_URL).getMessage());
    }
}