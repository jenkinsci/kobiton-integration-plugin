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

import java.io.File;

@Symbol("appUploaderBuilder")
@Extension
public class AppUploaderBuilderDescriptor extends BuildStepDescriptor<Builder> {
    public AppUploaderBuilderDescriptor() {
        super(AppUploaderBuilder.class);
        load();
    }

    public FormValidation doCheckUploadPath(@QueryParameter String uploadPath) {
        if (StringUtils.isNullOrEmpty(uploadPath)) {
            return FormValidation.error(Messages.UploadApp_error_missingUploadPath());
        }

        File file = new File(uploadPath);
        if (!file.exists()) {
            return FormValidation.error(Messages.UploadApp_error_fileNotFound());
        }

        String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        if (!Constants.SupportedFile.getExtensions().contains(fileExtension.toLowerCase())) {
            return FormValidation.error(Messages.UploadApp_error_fileUnsupportedFileExtension() + ": " + fileExtension);
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
