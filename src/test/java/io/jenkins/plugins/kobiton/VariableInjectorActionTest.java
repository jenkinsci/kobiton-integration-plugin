package io.jenkins.plugins.kobiton;

import hudson.EnvVars;
import hudson.model.Run;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class VariableInjectorActionTest {

    @Test
    void getters_ShouldWorkCorrectly() {
        Map<String, String> envVars = new HashMap<>();

        VariableInjectorAction action = new VariableInjectorAction(envVars);

        assertNull(action.getIconFileName());
        assertNull(action.getDisplayName());
        assertNull(action.getUrlName());
    }


    @Test
    void buildEnvironment_EnvVarsGiven_ShouldAddVarsToEnv() {
        Run<?, ?> run = mock(Run.class);
        EnvVars env = mock(EnvVars.class);
        Map<String, String> envVars = new HashMap<>();
        VariableInjectorAction action = new VariableInjectorAction(envVars);

        action.buildEnvironment(run, env);

        verify(env).putAll(envVars);
    }
}