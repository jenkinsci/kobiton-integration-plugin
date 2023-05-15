package io.jenkins.plugins.kobiton.services.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DefaultFileOperations implements FileOperations {
    @Override
    public byte[] readFileContent(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}
