package io.jenkins.plugins.kobiton;

import hudson.model.Descriptor;
import hudson.model.FreeStyleProject;
import io.jenkins.plugins.kobiton.services.user.DefaultUserService;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.kohsuke.stapler.StaplerRequest2;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@RunWith(MockitoJUnitRunner.class)
public class CredentialsBuildWrapperDescriptorTest {
    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();
    final String username = "username";
    final String apiKey = "apiKey";
    final String standaloneUrl = "standaloneUrl";

    @Test
    public void newInstance_FormDataGiven_ShouldReturnBuildWrapper() throws Descriptor.FormException {
        StaplerRequest2 request = mock(StaplerRequest2.class);
        JSONObject formData = mock(JSONObject.class);
        when(formData.getString("username")).thenReturn(username);
        when(formData.getString("apiKey")).thenReturn(apiKey);
        when(formData.getString("standaloneUrl")).thenReturn(standaloneUrl);

        Jenkins jenkins = jenkinsRule.getInstance();
        CredentialsBuildWrapperDescriptor descriptor = jenkins.getDescriptorByType(CredentialsBuildWrapperDescriptor.class);

        CredentialsBuildWrapper newInstance = descriptor.newInstance(request, formData);

        assertEquals(username, newInstance.getUsername());
        assertEquals(apiKey, newInstance.getApiKey());
        assertEquals(standaloneUrl, newInstance.getStandaloneUrl());
    }

    @Test
    public void BuildWrapperDescriptor_ShouldReturnCorrectly() throws IOException {
        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        Jenkins jenkins = jenkinsRule.getInstance();
        CredentialsBuildWrapperDescriptor descriptor = jenkins.getDescriptorByType(CredentialsBuildWrapperDescriptor.class);

        // getters, setters
        assertNotEquals("", descriptor.getDisplayName());
        assertTrue(descriptor.isApplicable(project));

        // doCheckUsername
        assertEquals("Username cannot be empty", descriptor.doCheckUsername("").getMessage());
        assertNull(descriptor.doCheckUsername(username).getMessage());

        // doCheckApiKey
        assertEquals("API key cannot be empty", descriptor.doCheckApiKey("").getMessage());
        assertNull(descriptor.doCheckApiKey(apiKey).getMessage());
    }

    @Test
    public void doAuthenticateUser_UsernameApiKeyAndUrlGiven_ShouldReturnMessageCorrectly() throws IOException, NoSuchFieldException, IllegalAccessException {
        DefaultUserService userServiceMock = mock(DefaultUserService.class);
        when(userServiceMock.isUserDisabled(any())).thenReturn(true);

        Jenkins jenkins = jenkinsRule.getInstance();
        CredentialsBuildWrapperDescriptor descriptor = jenkins.getDescriptorByType(CredentialsBuildWrapperDescriptor.class);

        Field userServiceField = CredentialsBuildWrapperDescriptor.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(descriptor, userServiceMock);

        assertEquals("Username and API key cannot be empty", descriptor.doAuthenticateUser(null, null, standaloneUrl).getMessage());
        assertEquals("Username and API key cannot be empty", descriptor.doAuthenticateUser(username, null, standaloneUrl).getMessage());
        assertEquals("Username and API key cannot be empty", descriptor.doAuthenticateUser(null, apiKey, standaloneUrl).getMessage());
        assertEquals("Unauthorized", descriptor.doAuthenticateUser(username, apiKey, standaloneUrl).getMessage());

        when(userServiceMock.isUserDisabled(any())).thenReturn(false);

        assertEquals("Success", descriptor.doAuthenticateUser(username, apiKey, standaloneUrl).getMessage());

        when(userServiceMock.isUserDisabled(any())).thenThrow(IOException.class);

        assertEquals("Unauthorized", descriptor.doAuthenticateUser(username, apiKey, standaloneUrl).getMessage());
    }
}