# Kobiton Jenkins Plugin

Kobiton is a mobile-first testing platform purpose-built for enterprises with the power to deliver faster release cycles and exceptional user experiences. As a mobile-centric testing platform, we pride ourselves on our top-tier customer service, platform capabilities, and product stability. Learn more about Kobiton [here](https://kobiton.com/).

This Kobiton Jenkins plugin allows you to integrate and run your automation test cases from a Jenkins CI server on Kobiton's device pool.

## Features

This plugin provides additional functionality for user to interact with Kobiton platform and services:
- Upload an application to the Kobiton Apps Repository.
- Upload a new version for an existing application in the Kobiton Apps Repository.
- Run appium tests with a specific app version on devices hosted by Kobiton

## Prerequisites

The following are required to use the plugin:

- An existing Jenkins CI server (version 2.387.3 or later).
- A Kobiton account. You can [sign-up for free trial](https://info.kobiton.com/sign-up) if you do not have an existing account.
- Other plugins:
  - [Jackson 2 API](https://plugins.jenkins.io/jackson2-api/).

## Installation and set up

### Get Kobiton Plugin

To run your Appium tests with Jenkins on Kobiton devices, you will first need to download our Jenkins plugin. Before installing the plugin, ensure you have the necessary privileges to administer your Jenkins installation. We recommend doing this when there are no active build jobs running on Jenkins.

1. Click on **Manage Jenkins** > **Manage Plugins**.
2. Select the **Available** tab.
3. In the search box type **Kobiton**.
4. Choose **Kobiton** from the list of available plugins.
       
### Install the plugin to your Jenkins instance

Navigate to your Jenkins Dashboard, click **Manage Jenkins**.

![jenkins-dashboard.png](assets%2Fjenkins-dashboard.png)

Go to **Manage Plugins** > **Advanced Settings**.

![manage-plugins.png](assets%2Fmanage-plugins.png)

![advanced-setting.png](assets%2Fadvanced-setting.png)

In the **Deploy Plugin** section, click **Choose File** and choose the downloaded plugin file. Then, click **Deploy**.

![upload-hpi.png](assets%2Fupload-hpi.png)

Restart Jenkins to activate the plugin.

### Install other necessary plugins

> 📝 **Note:** In this example, we will execute a Node.js script hosted on GitHub. We need the NodeJS plugin to support this. Depending on your use case, you may need to install other plugins and configure accordingly.

Access Jenkins, then click on **Manage Jenkins** > **Manage Plugins** → **Available Plugins**.

Search for **GitHub Integration Plugin** and **NodeJS**, select their checkboxes, then click **Install without restart**.

![install-plugin-nodejs.png](assets%2Finstall-plugin-nodejs.png)

Next, add a global configuration for Node.js. In the Jenkins dashboard, go to **Manage Jenkins** > **Global Tool Configuration**.

![global-tool-configuration.png](assets%2Fglobal-tool-configuration.png)

If NodeJS plugin is installed, the **NodeJS** section is available. Click **NodeJS Installations** > **Add NodeJS**, then input a name and choose the NodeJS version from the dropdown. Click **Save**.

![add-nodejs-runtime.png](assets%2Fadd-nodejs-runtime.png)

## How to Use

### Create and configure a Jenkins job

In the dashboard, click **+ New Item**. Enter a name for your job and choose **Freestyle project**, then click **OK**.

![add-job.png](assets%2Fadd-job.png)

### Add build environment variables

In the **Build Environment** section, select **Kobiton** checkbox, then add your Kobiton username and API key. You can find this in the [Portal](https://portal.kobiton.com/settings/keys).

![add-build-env.png](assets%2Fadd-build-env.png)

> 💡 **Tip:** You can click the **Validate** button to check if your credentials are correct.

### Add upload app build step

In the **Build Steps** section, click **Add build step**, then select **Upload application to Kobiton Apps Repository** from the dropdown list.

![add-build-step-upload-app.png](assets%2Fadd-build-step-upload-app.png)

### Add app path

Provide the local path to your application file.

![add-app-path.png](assets%2Fadd-app-path.png)

*Optional: If your application has already been published to Apps Repository, you can upload a new version by selecting **Create a new application version** checkbox. Then, provide the App ID (obtainable from the [App Tiles](https://support.kobiton.com/hc/en-us/articles/360056028911-Managing-Applications-#app-tiles-0-0) in the Portal).*

![create-new-app-ver.png](assets%2Fcreate-new-app-ver.png)

> 💡 **Tip:** You can click on the "?" icon to expand detailed help.

The Kobiton plugin will set these environment variables:
- `KOBITON_APP_ID`: The App ID of the uploaded application.
- `KOBITON_USERNAME`: Your Kobiton username.
- `KOBITON_API_KEY`: Your Kobiton API key.

You will need to use these environment variables to set the [desired capabilities](https://support.kobiton.com/hc/en-us/articles/360056024171-Desired-Capabilities-) in your script. Bellow is an example:

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

### Add automation test script

> 📝 **Note:** A Node.js script is used in this example. The actual steps may vary depending on the programming language and test framework of your scripts.

In **Build Steps** section, click **Add build step**, select **Execute NodeJS script** from the dropdown list.

![add-execute-nodejs-script.png](assets%2Fadd-execute-nodejs-script.png)

In **NodeJS Installation**, choose the name of NodeJS you had [configured in Global Tool Configuration](#install-other-necessary-plugins).

![choose-node-version.png](assets%2Fchoose-node-version.png)

Click **Add build step** to add nother step, then choose **Execute shell**.

![add-execute-shell.png](assets%2Fadd-execute-shell.png)

Then paste the script below into the field: 

> 📝 **Note:** The script used here is a sample Node.js test script from the Kobiton public repository. Replace this with your actual script.

```bash
if [ -d "test-sample" ]; then
    rm -rf "test-sample"
    echo "test-sample already exists. The folder was removed in order to clone a new one."
fi
git clone https://github.com/kobiton/samples.git
cd samples/javascript-jenkins
npm install
npm run android-app-test
```

Save the job configuration.

![add-execute-shell-script.png](assets%2Fadd-execute-shell-script.png)

### Execute job

Navigate back to the job's main page. Click **Build Now** to run the job.

![build-now.png](assets%2Fbuild-now.png)

When the job finishes, click the build result > **Console Output**. The result should be similar to the below:

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

You can use the above ID to view your test report in Kobiton Portal: https://portal.kobiton.com/sessions/{sessionId}. Replace `{sessionID}` with the actual ID you received.

## License

Licensed under MIT, see [LICENSE](LICENSE.md).

## Troubleshooting

If you encounter any issues or have questions, please:

- Check the [Troubleshooting](TROUBLESHOOTING.md) guide.
- Search the *GitHub Issues* to see if the issue has already been reported.
- Open a new issue if your problem is not yet addressed at [Jenkins issue tracker](https://issues.jenkins-ci.org/).


## Contributing to the Plugin

Refer to [contributing to the plugin](CONTRIBUTING.md) for contribution guidelines.
