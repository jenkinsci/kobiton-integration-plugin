package io.jenkins.plugins.kobiton;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import io.jenkins.plugins.kobiton.services.app.AppUploaderService;
import io.jenkins.plugins.kobiton.shared.models.Application;
import io.jenkins.plugins.kobiton.shared.models.PreSignedURL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import java.io.IOException;
import java.lang.reflect.Field;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
@WithJenkins
class AppUploaderBuilderTest {

    private static final String UPLOAD_PATH = "path/to/app";
    private static final Integer APP_ID = 123;
    private static final Integer VERSION_ID = 456;
    private static final Boolean IS_UPDATE_VERSION = true;

    private JenkinsRule jenkins;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        jenkins = rule;
    }

    @Test
    void Constructor_ShouldWorkCorrectly() {
        AppUploaderBuilder builder1 = new AppUploaderBuilder(UPLOAD_PATH, IS_UPDATE_VERSION, APP_ID);
        AppUploaderBuilder builder2 = new AppUploaderBuilder(UPLOAD_PATH, !IS_UPDATE_VERSION, APP_ID);

        assertEquals(UPLOAD_PATH, builder1.getUploadPath());
        assertEquals(APP_ID, builder1.getAppId());
        assertEquals(APP_ID, builder1.getAppId());
        assertNotEquals(IS_UPDATE_VERSION, builder2.getIsUpdateVersion());
    }

    @Test
    void perform_SuccessBuildGiven_ShouldLogCorrectly() throws Exception {
        PreSignedURL mockPreSignUrl = new PreSignedURL("appPath", "url");
        Application mockApplication = new Application(APP_ID, VERSION_ID);

        AppUploaderService appServiceMock = mock(AppUploaderService.class);
        when(appServiceMock.generatePreSignedUploadURL(any(), any(), any())).thenReturn(mockPreSignUrl);
        when(appServiceMock.uploadFileToS3(any(), any())).thenReturn(true);
        when(appServiceMock.createApplication(any(), any(), any())).thenReturn(mockApplication);

        AppUploaderBuilder builder = new AppUploaderBuilder(UPLOAD_PATH, IS_UPDATE_VERSION, APP_ID);
        Field appServiceField = AppUploaderBuilder.class.getDeclaredField("appService");
        appServiceField.setAccessible(true);
        appServiceField.set(builder, appServiceMock);

        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(builder);

        FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
        jenkins.assertLogContains("Upload application to Apps Repository successfully. Application details: ", build);
    }

    @Test
    void perform_FailedBuildGiven_ShouldLogError() throws Exception {
        AppUploaderService appServiceMock = mock(AppUploaderService.class);
        when(appServiceMock.generatePreSignedUploadURL(any(), any(), any())).thenThrow(IOException.class);

        AppUploaderBuilder builder = new AppUploaderBuilder(UPLOAD_PATH, IS_UPDATE_VERSION, APP_ID);
        Field appServiceField = AppUploaderBuilder.class.getDeclaredField("appService");
        appServiceField.setAccessible(true);
        appServiceField.set(builder, appServiceMock);

        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(builder);

        FreeStyleBuild build = jenkins.buildAndAssertStatus(Result.FAILURE, project);
        jenkins.assertLogContains("[ERROR]", build);
    }

    @Test
    void perform_NullAppUploaderGiven_ShouldInitService() throws Exception {
        AppUploaderBuilder builder = new AppUploaderBuilder(UPLOAD_PATH, IS_UPDATE_VERSION, APP_ID);
        Field appServiceField = AppUploaderBuilder.class.getDeclaredField("appService");
        appServiceField.setAccessible(true);
        appServiceField.set(builder, null);

        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(builder);

        jenkins.buildAndAssertStatus(Result.FAILURE, project);
    }
}