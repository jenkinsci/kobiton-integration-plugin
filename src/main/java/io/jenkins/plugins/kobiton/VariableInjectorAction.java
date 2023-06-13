package io.jenkins.plugins.kobiton;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.model.EnvironmentContributingAction;
import hudson.model.Run;

import java.util.HashMap;
import java.util.Map;

public class VariableInjectorAction implements EnvironmentContributingAction {
    private Map<String, String> envVars = new HashMap<>();

    public VariableInjectorAction(Map<String, String> envVars) {
        this.envVars = envVars;
    }
    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getUrlName() {
        return null;
    }

    @Override
    public void buildEnvironment(@NonNull Run<?, ?> run, @NonNull EnvVars env) {
        EnvironmentContributingAction.super.buildEnvironment(run, env);
        env.putAll(envVars);
    }
}
