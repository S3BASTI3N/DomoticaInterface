## TODO

#### List

##### App side

- construct database
- write accessor functions to the database
- write a-sync functions to write changes to the database
- write a-sync functions to write changes to the server
- write broadcast receiver which will wait for messages from the server
- write service which will be waiting for messages from the broadcast receiver
- let broadcast receiver start a service which will write the changes to the database
- let the service alarm the notification center when a notification is required

#### Server side

- let server send a push notification to the application
- display all messages from the application in the server console
- send a full database to the application when requested
- execute the application messages
- handle page updates when there has been a database change


#### Intentions
 - When an object such as a group or device is retrieved from the database this will be done without
 background tasks.

 - When data is written to the database or server this will be done on a background thread to prevent lag.
 Callback functions from background threads will be minimalist.

