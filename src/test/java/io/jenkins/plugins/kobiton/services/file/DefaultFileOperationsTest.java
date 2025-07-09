package io.jenkins.plugins.kobiton.services.file;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DefaultFileOperationsTest {

    @Test
    void readFileContent_InvalidFilePathGiven_ShouldThrowError() throws IOException {
        String directory = new File("./").getAbsolutePath();
        String repoDir = directory.substring(0, directory.length() - 1);
        String filePath = repoDir + "src/test/resources/io/jenkins/plugins/kobiton/services/file/test.txt";
        byte[] expectedContent = "test".getBytes();

        DefaultFileOperations fileOperations = new DefaultFileOperations();
        byte[] actualContent = fileOperations.readFileContent(filePath);

        assertArrayEquals(expectedContent, actualContent);
    }
}