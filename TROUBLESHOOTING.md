# Troubleshooting Guide for Kobiton Plugin

This guide provides troubleshooting steps for common issues encountered while using the Kobiton plugin in Jenkins.

## Issues

### Timeout when running job

```bash
Ending your web drivage..

quit driver: Error [ERR_SOCKET_CONNECTION_TIMEOUT]: [quit()] Socket connection timeout
```

If you encounter this output in the console log when running the job, please check if you need access to the VPN.

### Incompatible version

```bash 
FATAL: command execution failed
java.io.IOException: Failed to install https://nodejs.org/dist/v14.0.0/node-v14.0.0-darwin-arm64.tar.gz

Build step 'Execute NodeJS script' marked build as failure
```

If you encounter this output in the console log, make sure you have installed a stable Node.js version (this example is using version 19.7.0).

It is important to also make sure your `npm` version is correct:

```bash
$ npm -v
8.5.1
```