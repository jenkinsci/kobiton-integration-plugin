package io.jenkins.plugins.kobiton.services.file;

import java.io.IOException;

public interface FileOperations {
    byte[] readFileContent(String filePath) throws IOException;
}
