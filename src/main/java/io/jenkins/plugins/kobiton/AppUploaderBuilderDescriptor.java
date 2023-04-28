package io.jenkins.plugins.kobiton;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.plugins.kobiton.shared.constants.Constants;
import io.jenkins.plugins.kobiton.shared.utils.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Symbol("appUploaderBuilder")
@Extension
public class AppUploaderBuilderDescriptor extends BuildStepDescriptor<Builder> {
    public AppUploaderBuilderDescriptor() {
        super(AppUploaderBuilder.class);
        load();
    }

    public FormValidation doCheckUploadPath(@QueryParameter String uploadPath) throws IOException, ServletException {
        if (StringUtils.isNullOrEmpty(uploadPath)) {
            return FormValidation.error(Messages.UploadApp_error_missingUploadPath());
        }

        File file = new File(   uploadPath);
        if (!file.exists()) {
                return FormValidation.error(Messages.UploadApp_error_fileNotFound());
        }

        String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        Path filePath = Paths.get(file.getAbsolutePath());
        String fileMimeType = Files.probeContentType(filePath);
        if (
                !Constants.SupportedFile.getExtensions().contains(fileExtension.toLowerCase()) &&
                !Constants.SupportedFile.getMimeTypes().contains(fileMimeType)
        ) {
            return FormValidation.error(Messages.UploadApp_error_fileUnsupportedMimeType() + ": " + fileMimeType);
        }

        return FormValidation.ok();
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }

    @NonNull
    @Override
    public String getDisplayName() {
        return Messages.UploadApp_DisplayName();
    }

}
