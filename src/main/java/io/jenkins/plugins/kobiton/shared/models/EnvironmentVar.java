package io.jenkins.plugins.kobiton.shared.models;

/**
 * Environment variables
 */
public interface EnvironmentVar {
    /**
     * internal environment
     */
    String USERNAME = "USERNAME";
    String API_KEY = "API_KEY";
    /**
     * external environment for the next build
     */
    String KOBITON_USERNAME = "KOBITON_USERNAME";
    String KOBITON_API_KEY = "KOBITON_API_KEY";
    String KOBITON_APP_ID = "KOBITON_APP_ID";
}
