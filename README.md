# Kobiton Plugin

This plugin provides additional functionality for user to interact with Kobiton platform and services:
- Upload application to Kobiton Apps Repository.
- Update version for application in Kobiton Apps Repository.

## Dependencies

- Jenkins: The plugin requires Jenkins version 2.361.4 or later. 
- Other plugins:
  - [Structs](https://plugins.jenkins.io/structs/) >= 1.20
  - [Pipeline: Groovy](https://plugins.jenkins.io/workflow-cps/) >= 2.90
  - [Pipeline: Job](https://plugins.jenkins.io/workflow-job/) >= 2.40
  - [Pipeline: Basic Steps](https://plugins.jenkins.io/workflow-basic-steps/) >= 2.23
  - [Pipeline: Nodes and Processes](https://plugins.jenkins.io/workflow-durable-task-step/) >= 2.37

## Installation

### Set up Jenkins

Follow this [tutorial](https://phoenixnap.com/kb/install-jenkins-on-mac]) to install Jenkins on your local machine. You can use other methods to install Jenkins, like Docker, for instance.

After setting up Jenkins, you can access it on your local: http://localhost:8080/.

### Get Kobiton Plugin

> ℹ️ **Info**: You can choose one of the following options: 
> - Download the plugin directly.
> - Build the plugin from the source code.

#### Direct download

Download the latest release from [Releases](https://github.com/jenkins-integration/repository/releases) page.

Or you can find the `jenkins-integration.hpi` file in `assets/` folder.

#### Build the plugin from the source code

Make sure you have Maven and JDK installed, you can follow this [tutorial](https://www.digitalocean.com/community/tutorials/install-maven-mac-os).

```bash
java -version  # check if Java is installed
mvn −version  # check if Maven is installed
```

Clone this repository. Navigate to the root directory of the repository.

```bash
git clone git@github.com:kobiton/jenkins-integration.git
cd jenkins-integration
```

Run `mvn package` to build the plugin. This will create a `jenkins-integration.hpi` file at `target/` folder.

```bash 
mvn package
```
       
### Install the plugin to your Jenkins instance

Navigate to your Jenkins Dashboard, click **Manage Jenkins**.

![jenkins-dashboard.png](assets%2Fjenkins-dashboard.png)

Go to **Manage Plugins** → **Advanced Settings**.

![manage-plugins.png](assets%2Fmanage-plugins.png)

![advanced-setting.png](assets%2Fadvanced-setting.png)

In the **Deploy Plugin** section, click **Choose File** and choose the downloaded plugin file. Then, click **Deploy**.

![upload-hpi.png](assets%2Fupload-hpi.png)

Restart Jenkins to activate the plugin.

### Install other necessary plugins

> 📝 **Note:** In this example, we will execute a Node.js script hosted on GitHub. So we will need plugins to support this. Depending on your use case, you may need to install other plugins and configure accordingly.

Access Jenkins → Click on **Manage Jenkins** → **Manage Plugins** → **Available Plugins**.

Search for **GitHub Integration Plugin** and **NodeJS** → select their checkboxes → click **Install without restart**.

![install-plugin-nodejs.png](assets%2Finstall-plugin-nodejs.png)

Then, we need to add a global configuration for Node.js. Back from your Jenkins dashboard, go to **Manage Jenkins** → **Global Tool Configuration**.

![global-tool-configuration.png](assets%2Fglobal-tool-configuration.png)

If you have installed NodeJS plugin successfully, you will see **NodeJS** section. Click **NodeJS Installations** → **Add NodeJS**. Add a name and choose version from the dropdown, then click **Save**.

![add-nodejs-runtime.png](assets%2Fadd-nodejs-runtime.png)

## How to Use

### Create and configure a Jenkins job

In the dashboard, click **+ New Item**. Then enter a name for your job and choose **Freestyle project**. Then, click **OK**.

![add-job.png](assets%2Fadd-job.png)

### Add build environment variables

In the **Build Environment** section, select **Kobiton** checkbox, then add your Kobiton username and API key. You can find this in the [Portal](https://portal.kobiton.com/settings/keys).

![add-build-env.png](assets%2Fadd-build-env.png)

> 💡 **Tip:** You can click **Validate** button to check if your credentials are correct.

### Add upload app build step

In **Build Steps** section, click **Add build step**, select **Upload application to Kobiton Apps Repository** from the dropdown list.

![add-build-step-upload-app.png](assets%2Fadd-build-step-upload-app.png)

### Add app path

Provide the path to your application file.

![add-app-path.png](assets%2Fadd-app-path.png)

*Optional: If your application has been published to Apps Repository, you can update the version by selecting **Create a new application version** checkbox. Then, provide the application ID.*

![create-new-app-ver.png](assets%2Fcreate-new-app-ver.png)

> 💡 **Tip:** You can click on the "?" icon to expand detailed help.

The Kobiton plugin will set these environment variables:
- `KOBITON_APP_ID`: The ID of the uploaded application.
- `KOBITON_USERNAME`: Your Kobiton username.
- `KOBITON_API_KEY`: Your Kobiton API key.

You will need to use these environment variables to set the desired capabilities in your script. Bellow is an example:

```javascript
const username = process.env.KOBITON_USERNAME
const apiKey = process.env.KOBITON_API_KEY
const appId = process.env.KOBITON_APP_ID

const desiredCaps = {
  sessionName:        'Automation test session - App',
  sessionDescription: 'This is an example for Android app',
  deviceOrientation:  'portrait',
  captureScreenshots: true,
  deviceName:         'Galaxy*',
  platformName:       'Android',
  app:                appId,
}
```

### Add Node.js script

> 📝 **Note:** Below are the steps for executing a Node.js script. Depending on your specific use case, you may need to follow different steps accordingly.

In **Build Steps** section, click **Add build step**, select **Execute NodeJS script** from the dropdown list.

![add-execute-nodejs-script.png](assets%2Fadd-execute-nodejs-script.png)

In **NodeJS Installation**, choose the name of NodeJS you had [configured in Global Tool Configuration](#install-other-necessary-plugins).

![choose-node-version.png](assets%2Fchoose-node-version.png)

**Add build step** → Choose **Execute shell**.

![add-execute-shell.png](assets%2Fadd-execute-shell.png)

Then paste the script below into field *(a sample Node.js test script in GitHub repository)*.

```bash
if [ -d "test-sample" ]; then
    rm -rf "test-sample"
    echo "test-sample already exists. The folder was removed in order to clone a new one."
fi
git clone https://github.com/huytunguyenn/test-sample.git
cd test-sample
npm install
npm run android-app-test
```

Save the job configuration.

![add-execute-shell-script.png](assets%2Fadd-execute-shell-script.png)

### Execute job

Back to the job. Click **Build Now** to run the job and see the plugin in action.

![build-now.png](assets%2Fbuild-now.png)

Click on the build result → **Console Output**, the result should be like below 👀:

```bash
...

Upload application to Apps Repository successfully. Application details: { versionId='620626' }.

...

 > CALL init({"sessionName":"Automation test session - App","sessionDescription":"This is an example for Android app","deviceOrientation":"portrait","captureScreenshots":true,"deviceName":"Galaxy*","platformName":"Android","app":"kobiton-store:v620626"}) 
 > POST /session {"desiredCapabilities":{"sessionName":"Automation test session - App","sessionDescription":"This is an example for Android app","deviceOrientation":"portrait","captureScreenshots":true,"deviceName":"Galaxy*","platformName":"Android","app":"kobiton-store:v620626"}}

...

SessionID used for the next step 5505018
    ✓ should open the app
 > CALL quit() 
 > DELETE /session/:sessionID 
 
 ...
 
   1 passing (24s)

Finished: SUCCESS
```

The session ID is `5505018` in this sample output. 

You can use the number to view your test report in Kobiton page: https://portal.kobiton.com/sessions/{sessionId}

## License

Licensed under MIT, see [LICENSE](LICENSE.md).

## Troubleshooting

If you encounter any issues or have questions, please:

- Check the [Troubleshooting](TROUBLESHOOTING.md) guide.
- Search the *GitHub Issues* to see if the issue has already been reported.
- Open a new issue if your problem is not yet addressed at [Jenkins issue tracker](https://issues.jenkins-ci.org/).


## Contributing to the Plugin

Refer to [contributing to the plugin](CONTRIBUTING.md) for contribution guidelines.