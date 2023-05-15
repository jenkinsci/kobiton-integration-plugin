package io.jenkins.plugins.kobiton;

import hudson.util.Secret;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import io.jenkins.plugins.kobiton.services.user.DefaultUserService;
import io.jenkins.plugins.kobiton.shared.logger.PluginLogger;
import io.jenkins.plugins.kobiton.shared.models.Credential;
import io.jenkins.plugins.kobiton.shared.utils.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

@Symbol("credentialsBuildWrapper")
@Extension
public class CredentialsBuildWrapperDescriptor extends BuildWrapperDescriptor {
    DefaultUserService userService = new DefaultUserService();

    public CredentialsBuildWrapperDescriptor() {
        super(CredentialsBuildWrapper.class);
        load();
    }

    @NonNull
    @Override
    public String getDisplayName() {
        return Messages.BuildEnvironment_DisplayName();
    }

    @Override
    public boolean isApplicable(AbstractProject<?, ?> item) {
        return true;
    }

    @Override
    public CredentialsBuildWrapper newInstance(StaplerRequest req, JSONObject formData) throws FormException {
        return new CredentialsBuildWrapper(
                formData.getString("username"),
                Secret.fromString(formData.getString("apiKey")),
                formData.getString("standaloneUrl"));
    }

    public FormValidation doCheckUsername(@QueryParameter String username) {
        return StringUtils.isNullOrEmpty(username)
                ? FormValidation.error(Messages.BuildEnvironment_error_missingUsername()) : FormValidation.ok();
    }

    public FormValidation doCheckApiKey(@QueryParameter String apiKey) {
        return StringUtils.isNullOrEmpty(apiKey)
                ? FormValidation.error(Messages.BuildEnvironment_error_missingApiKey()) : FormValidation.ok();
    }

    public FormValidation doAuthenticateUser(@QueryParameter("username") final String username,
                                             @QueryParameter("apiKey") final String apiKey,
                                             @QueryParameter("standaloneUrl") final String standaloneUrl) {

        if (StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(apiKey)) {
            return FormValidation.error(Messages.BuildEnvironment_error_missingCredential());
        }

        ApiEndpoint.getInstance().setBaseUrl(standaloneUrl);

        try {
            Boolean isUserDisabled = userService.isUserDisabled(new Credential(username, apiKey));
            if (Boolean.TRUE.equals(isUserDisabled)) {
                return FormValidation.error(Messages.BuildEnvironment_auth_fail());
            }
            return FormValidation.ok(Messages.BuildEnvironment_auth_success());

        } catch(Exception e) {
            PluginLogger.debug("Error while validating: " + e.getMessage(), AppUploaderBuilder.class.getName());
            return FormValidation.ok(Messages.BuildEnvironment_auth_fail());
        }
    }
}
