# Design Document
------------------

## Demonstration Server

The demonstration server can be found [here](http://www.uvaatwork.nl/DomoticaInterface/index.php).

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

