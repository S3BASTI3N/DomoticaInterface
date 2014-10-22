package net.cs76.projects.student10340912.DomoticaInterface.utils;

/**
 * Created by sebastien on 14-10-14.
 */
public class Device {

    public int id_;
    public String name_;
    public int state_;
    public int notification_;

    public static int NOTIFICATION_ON = 1;
    public static int NOTIFICATION_OFF = 0;

    public Device( int id, String name, int state, int notification ) {

        id_ = id;
        name_ = name;
        state_ = state;
        notification_ = notification;

    }
}
