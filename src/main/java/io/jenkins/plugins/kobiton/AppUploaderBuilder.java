package io.jenkins.plugins.kobiton;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import io.jenkins.plugins.kobiton.services.app.AppUploaderService;
import io.jenkins.plugins.kobiton.shared.logger.PluginLogger;
import io.jenkins.plugins.kobiton.shared.models.*;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class AppUploaderBuilder extends Builder implements SimpleBuildStep {
    private final String uploadPath;
    private final Integer appId;
    private final Boolean isUpdateVersion;
    AppUploaderService appService;

    @DataBoundConstructor
    public AppUploaderBuilder(String uploadPath, Boolean isUpdateVersion, Integer appId) {
        this.uploadPath = uploadPath;
        this.isUpdateVersion = isUpdateVersion;
        if (Boolean.TRUE.equals(isUpdateVersion)) {
            this.appId = appId;
        } else {
            this.appId = null;
        }
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public Integer getAppId() {
        return appId;
    }

    public Boolean getIsUpdateVersion() {
        return isUpdateVersion;
    }

    private PreSignedURL preSignS3URL(AppUploaderService appService,
                                      String fileName,
                                      Integer appId,
                                      Credential credential) throws IOException {
        return appService.generatePreSignedUploadURL(credential, fileName, appId);
    }

    private Boolean uploadFileToS3 (AppUploaderService appService, String preSignedURL) throws IOException {
        return appService.uploadFileToS3(preSignedURL, uploadPath);
    }

    private Application createNewApplication(AppUploaderService appService,
                                             Credential credential,
                                             String fileName,
                                             String appPath) throws IOException {
        return appService.createApplication(credential, fileName, appPath);
    }

    private void injectEnvironmentVariables(Run<?, ?> run, String appId, Credential credential) {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put(EnvironmentVar.KOBITON_USERNAME, credential.getUsername());
        envVariables.put(EnvironmentVar.KOBITON_API_KEY, credential.getApiKey());
        envVariables.put(EnvironmentVar.KOBITON_APP_ID, appId);

        VariableInjectorAction variableInjectorAction = new VariableInjectorAction(envVariables);
        run.addAction(variableInjectorAction);
    }

    @Override
    public void perform(@NonNull Run<?, ?> run,
                        @NonNull FilePath workspace,
                        @NonNull EnvVars env,
                        @NonNull Launcher launcher,
                        TaskListener listener) throws InterruptedException, IOException {
        PrintStream logger = listener.getLogger();
        Credential credential = new Credential(env.get(EnvironmentVar.USERNAME), env.get(EnvironmentVar.API_KEY));
        String fileName = uploadPath.substring(uploadPath.lastIndexOf("/") + 1);

        try {
            if (appService == null) {
                appService = new AppUploaderService();
            }

            PreSignedURL preSignedURL = preSignS3URL(appService, fileName, appId, credential);
            PluginLogger.debug("Pre-sign an URL successfully.", AppUploaderBuilder.class.getName());

            uploadFileToS3(appService, preSignedURL.url());
            PluginLogger.debug("Upload file to S3 successfully.", AppUploaderBuilder.class.getName());

            Application app = createNewApplication(appService, credential, fileName, preSignedURL.appPath());
            PluginLogger.log("Upload application to Apps Repository successfully. Application details: "
                    + app + ".", logger);

            injectEnvironmentVariables(run, app.getAppOrVersionId(), credential);

        } catch (Exception e) {
            PluginLogger.error("Fail while process uploading app.", AppUploaderBuilder.class.getName(), logger);
            throw e;
        }
    }
}
