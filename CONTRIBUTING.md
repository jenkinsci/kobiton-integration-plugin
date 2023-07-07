# Contributing to the Kobiton Plugin

Follow the below guidelines to contribute to the project.

## Development

### Build the plugin from the source code

Make sure you have Maven and JDK installed, you can follow this [tutorial for MacOS](https://www.digitalocean.com/community/tutorials/install-maven-mac-os).

```bash
java -version  # check if Java is installed
mvn −version  # check if Maven is installed
```

Clone this repository. Navigate to the root directory of the repository.

```bash
git clone git@github.com:kobiton/jenkins-integration.git
cd jenkins-integration
```

### Useful commands

- `mvn clean`: clean up the project by deleting the `target/` folder
- `mvn hpi:run`: run Jenkins with the plugin installed on port 8080 (to customize port, run `mvn hpi:run -Djetty.port=8081`)
- `mvn package`: build the plugin to `.hpi` file at `target/` folder
- `mvn compile`: compiles the source Java classes of the project
- `mvn verify`: run all tests
- `mvn clean -P enable-jacoco test jacoco:report`: run all tests and generate test coverage report (at `/target/site/jacoco/index.html`)

## Troubleshooting

### Unit tests fail to run

Follow these steps: `mvn clean`  →  `mvn compile`  →  `mvn test`. Make sure you have `/target/tmp` and `/target/test-classes` folder.

### "this.jenkins" is null error in unit tests

Use `import org.junit.Test;` instead of `import org.junit.jupiter.api.Test;`.
