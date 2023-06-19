# Contributing to the Kobiton Plugin

This is contribution guidelines.

## Development

### Useful commands

- `mvn clean`: clean up the project by deleting the `target/` folder
- `mvn hpi:run`: run Jenkins with the plugin installed on port 8080 (to customize port, run `mvn hpi:run -Djetty.port=8081`)
- `mvn package`: build the plugin to `.hpi` file at `target/` folder
- `mvn compile`: compiles the source Java classes of the project
- `mvn verify`: run all tests
- `mvn clean -P enable-jacoco test jacoco:report`: run all tests and generate test coverage report (at `/target/site/jacoco/index.html`)

## Troubleshooting

### Fail running unit tests

Try follow these steps: `mvn clean`  →  `mvn compile`  →  `mvn test`. Make sure you have `/target/tmp` and `/target/test-classes` folder.

### "this.jenkins" is null error in unit tests

Try to use `import org.junit.Test;` instead of `import org.junit.jupiter.api.Test;`.
