# Design Document
------------------

## Demonstration Server

The demonstration server can be found [here](http://www.uvaatwork.nl/DomoticaInterface/index.php).

## Server to device communication ##

Several methods of server to device communication have been attempted. First an attempt was made to
implement the Google Cloud Messaging for Android. After following different tutorials, one of which
Googles own, no server communication was achieved. Next a more simple, yet less immediate approach was tried. Using
the AlarmManager a service could be started which would ping the server for updates. After even more attempts
 no service could be started with the AlarmManager and thus no Server to device communication. Alas, the application checks
 for updates every time a new activity is started.

### Dataflow between the different classes.

![](../images/Dataflow.png)

## Mockups of the different activities.

#### HomeActivity

![](../images/HomeScreen.png)

#### GroupActivity

![](../images/GroupScreen.png)

#### DeviceActivity

![](../images/DeviceScreen.png)

#### LogicActivity

![](../images/LogicScreen.png)

#### RuleActivity

![](../images/RuleScreen.png)

### Database design

**Devices**
id (int), name(String), state(int), color(int), notification(boolean)

**groups**
id(int), name(String)

**relation_group_device**
group_id(int), device_id(int)

**logic_rule**
id(int), name(String), condition_id(int), action_id(int)

**logic_rule_device_state_describer**
id(int), device_id(int), state(int)



### Libraries

Toast, for on activity notifications.

