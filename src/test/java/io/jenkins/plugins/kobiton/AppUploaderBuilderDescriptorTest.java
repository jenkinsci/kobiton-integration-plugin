package io.jenkins.plugins.kobiton;

import hudson.model.FreeStyleProject;
import jenkins.model.Jenkins;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import static org.junit.jupiter.api.Assertions.*;

@WithJenkins
class AppUploaderBuilderDescriptorTest {

    private JenkinsRule jenkinsRule;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        jenkinsRule = rule;
    }

    @Test
    void builderDescriptor_ShouldReturnCorrectly() {
        String directory = new File("./").getAbsolutePath();
        String repoDir = directory.substring(0, directory.length() - 1);
        String textFilePath = repoDir + "src/test/resources/io/jenkins/plugins/kobiton/services/file/test.txt";
        String apkFilePath = repoDir + "src/test/resources/io/jenkins/plugins/kobiton/services/file/test.apk";
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