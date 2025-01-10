/*
 See the documentation for more options:
 https://github.com/jenkins-infra/pipeline-library/
*/
buildPlugin(
  useContainerAgent: true, // Set to `false` if you need to use Docker for containerized test
  configurations: [
    [platform: 'linux', jdk: 17], // use 'docker' if you have containerized tests
    [platform: 'windows', jdk: 21],
])
