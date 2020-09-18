# GPostCard

### Project Description

GPostcard is a Web application that can generate postcards based on the text that is on them. After a postcard is generated, it can be sent to any Gmail user. By ticking the location, the user can get on the postcard a representative photo for their current location. Moreover, the user can record a message which will be automatically filled into the text area allowing accessability. 

#### Goals

* Text analysis
* Image analysis
* Generate relevant postcards
* Design variability
* Use API pre-trained models

#### NON-Goals

* Store user data
* Use images that are not free for comercial use

### Prerequisites

#### Download Maven

This sample uses the [Apache Maven](https://maven.apache.org/) build system. Before getting started, be sure to download and install it. When you use Maven as described here, it will automatically download the needed client libraries.

#### Set Up to Authenticate With Your Project's Credentials

Please follow the [Set Up Your Project](https://cloud.google.com/natural-language/docs/quickstart#set_up_your_project) steps in the Quickstart doc to create a project and enable the Cloud Natural Language API. Following those steps, make sure that you [Set Up a Service Account](https://cloud.google.com/docs/authentication#getting_credentials_for_server-centric_flow), and export the following environment variable:

`export GOOGLE_APPLICATION_CREDENTIALS=/path/to/your-project-credentials.json`

### Set Up

To get started:

* Login to [Google Cloud Shell](https://ssh.cloud.google.com/cloudshell/editor)
* Clone this repo: `cd; git clone https://github.com/googleinterns/step230-2020.git`

To run the project:

* cd into the directory where the `pom.xml` is and then run: `mvn package appengine:run`

### Contributing

* See the [Contributing Guide](https://github.com/GoogleCloudPlatform/java-docs-samples/blob/master/CONTRIBUTING.md)

### License

* See [License](https://github.com/GoogleCloudPlatform/java-docs-samples/blob/master/LICENSE)
