## DomoticaInterface
----------------------

The test server can be found here: http://uvaatwork.nl/DomoticaInterface/

Domotica is all about connecting every single electronic device to the internet in order to enable
easy access over the internet. Once these devices are connected a user friendly interface
is required to manipulate them. This application provides this interface.

The app contains two modes: display and edit.

The display mode show the status off all available devices in naturally ordered menus and allows the user
to manipulate the devices state (switching on/off).

In the edit mode the user can edit rooms and properties of the device. The interface here also allows the user
to combine certain switches with certain devices.

#### Features

##### General

- The app should support simple on/off devices, rgb lights and on/off switches.

- A light should have the following properties: id, room, status (on/off), name.
- A RGB lights should have the following properties: id, room, red, green, blue, intensity, name.
- A switch should have the following properties: id, room, status (on/off), name.

- The app should allow the user to provide credentials which allow the app to access an online database
which contains all available devices of the corresponding user.

- The app should allow push notifications from the server when the state of a device is altered.

##### Display mode

- Display all rooms in an activity and allow the user to select a room.

- Allow the user to view all available devices in a single room and allow the user to select a single device.

- The status of the device should be displayed in the list with a on/off symbol or a color for the rgb lights.

- The user should be able to change the status of the device in the list view and in the detailed device activity.

- The user should be able to view the details of a single device in an activity.

##### Edit mode

- The user should be able to view all switch-light combinations in a single list and in a per room view.

- The user should be able to add, edit and remove switch-light combinations.

- All changes made by the user are pushed to the server where they are stored and taken into production
immediately.


#### Additional option features (when time allows it)

- Allow the user to add schedule based events. (For example every day at 8 am turn on light1)

- Allow the user to set notifications for certain state changes for particular switches.

- Allow the user to create more complex diagram which trigger lights. (For example: if switch1 is off and
switch2 is on turn light1 on)