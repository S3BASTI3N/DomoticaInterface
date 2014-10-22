package net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects;

import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.Device;

/**
 * Created by sebastien on 22-10-14.
 */
public class LogicRuleStateDescriber {

    public int id_;
    public Device device_;
    public int state_;

    public LogicRuleStateDescriber( int id, Device device, int state ) {

        id_ = id;
        device_ = device;
        state_ = state;

    }

    public String toString() {
        return "StateDescriber: id(" + id_ + ")  deviceId(" + device_.id_ + ")  state(" + state_ + ")";
    }
}
