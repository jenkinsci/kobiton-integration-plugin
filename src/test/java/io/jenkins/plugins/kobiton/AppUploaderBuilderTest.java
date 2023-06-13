package io.jenkins.plugins.kobiton;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import io.jenkins.plugins.kobiton.services.app.AppUploaderService;
import io.jenkins.plugins.kobiton.shared.models.Application;
import io.jenkins.plugins.kobiton.shared.models.PreSignedURL;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@RunWith(MockitoJUnitRunner.class)
public class AppUploaderBuilderTest {
    @Rule
    public JenkinsRule jenkins = new JenkinsRule();
    final String uploadPath = "path/to/app";
    final Integer appId = 123;
    final Integer versionId = 456;
    final Boolean isUpdateVersion = true;

    @Test
    public void Constructor_ShouldWorkCorrectly() {
        AppUploaderBuilder builder1 = new AppUploaderBuilder(uploadPath, isUpdateVersion, appId);
        AppUploaderBuilder builder2 = new AppUploaderBuilder(uploadPath, !isUpdateVersion, appId);

        assertEquals(builder1.getUploadPath(), uploadPath);
        assertEquals(builder1.getAppId(), appId);
        assertEquals(builder1.getAppId(), appId);
        assertNotEquals(builder2.getIsUpdateVersion(), isUpdateVersion);
    }

    @Test
    public void perform_SuccessBuildGiven_ShouldLogCorrectly() throws Exception {
        PreSignedURL mockPreSignUrl = new PreSignedURL("appPath", "url");
        Application mockApplication = new Application(appId, versionId);

        AppUploaderService appServiceMock = mock(AppUploaderService.class);
        when(appServiceMock.generatePreSignedUploadURL(any(), any(), any())).thenReturn(mockPreSignUrl);
        when(appServiceMock.uploadFileToS3(any(), any())).thenReturn(true);
        when(appServiceMock.createApplication(any(), any(), any())).thenReturn(mockApplication);

        AppUploaderBuilder builder = new AppUploaderBuilder(uploadPath, isUpdateVersion, appId);
        Field appServiceField = AppUploaderBuilder.class.getDeclaredField("appService");
        appServiceField.setAccessible(true);
        appServiceField.set(builder, appServiceMock);

        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(builder);

        FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
        jenkins.assertLogContains("Upload application to Apps Repository successfully. Application details: ", build);
    }

    @Test
    public void perform_FailedBuildGiven_ShouldLogError() throws Exception {
        AppUploaderService appServiceMock = mock(AppUploaderService.class);
        when(appServiceMock.generatePreSignedUploadURL(any(), any(), any())).thenThrow(IOException.class);

        AppUploaderBuilder builder = new AppUploaderBuilder(uploadPath, isUpdateVersion, appId);
        Field appServiceField = AppUploaderBuilder.class.getDeclaredField("appService");
        appServiceField.setAccessible(true);
        appServiceField.set(builder, appServiceMock);

        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(builder);

        FreeStyleBuild build = jenkins.buildAndAssertStatus(Result.FAILURE, project);
        jenkins.assertLogContains("[ERROR]", build);
    }

    @Test
    public void perform_NullAppUploaderGiven_ShouldInitService() throws Exception {
        AppUploaderBuilder builder = new AppUploaderBuilder(uploadPath, isUpdateVersion, appId);
        Field appServiceField = AppUploaderBuilder.class.getDeclaredField("appService");
        appServiceField.setAccessible(true);
        appServiceField.set(builder, null);

        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(builder);

        jenkins.buildAndAssertStatus(Result.FAILURE, project);
    }
}