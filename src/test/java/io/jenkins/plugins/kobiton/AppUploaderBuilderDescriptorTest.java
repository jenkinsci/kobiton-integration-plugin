package io.jenkins.plugins.kobiton;

import hudson.model.FreeStyleProject;
import jenkins.model.Jenkins;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class AppUploaderBuilderDescriptorTest {
    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    @Test
    public void BuilderDescriptor_ShouldReturnCorrectly() {
        String directory = new File("./").getAbsolutePath();
        String repoDir = directory.substring(0, directory.length() - 1);
        String textFilePath = repoDir + "src/test/java/io/jenkins/plugins/kobiton/services/file/test.txt";
        String apkFilePath = repoDir + "src/test/java/io/jenkins/plugins/kobiton/services/file/test.apk";
        Jenkins jenkins = jenkinsRule.getInstance();
        AppUploaderBuilderDescriptor descriptor = jenkins.getDescriptorByType(AppUploaderBuilderDescriptor.class);

        // getters, setters
        assertNotEquals("", descriptor.getDisplayName());
        assertTrue(descriptor.isApplicable(FreeStyleProject.class));

        // doCheckUploadPath
        assertEquals("Path cannot be empty when uploading file", descriptor.doCheckUploadPath("").getMessage());
        assertEquals("Uploading file does not exist", descriptor.doCheckUploadPath("invalid-path").getMessage());
        assertEquals("Unsupported file extension: txt", descriptor.doCheckUploadPath(textFilePath).getMessage());
        assertNull(descriptor.doCheckUploadPath(apkFilePath).getMessage());
    }
}