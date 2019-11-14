# Stamp Android

Android application for the digital stamp card service.

> Note! The project should be considered as working prototype for the time being. There is known bugs and some features are not yet implemented. I'm working on those alongside improving the code to better follow Android's design guidelines.

## Test the app
After you've cloned the repo, just open it in Android Studio. It should download all the necessary files and let you build the app. However, before testing, it is recommended to run the [API server](https://github.com/jumakall/stamp-api) somewhere and point the application to use that server. Otherwise you can't get past the login screen.

Instruct the app to use the correct API server by modifying the ``BASE_URL`` variable in the ``StampApiService.kt`` file. Modern versions of Android block HTTP traffic by default. If you are required to use HTTP protocol instead of HTTPS, you should modify ``android:usesCleartextTraffic`` accordingly in the ``AndroidManifest.xml`` file.

:exclamation:WARNING:exclamation: Never use HTTP over internet, since all traffic will be transmitted as cleartext, including passwords.

## Dealing with unimplemented features
Some relevant features are not yet implemented, like user registration and pass creation. Those can be circumvented by crafting a proper API requests and sending them to the API server. More about how the requests should be constructed in the [API server](https://github.com/jumakall/stamp-api) repository.
