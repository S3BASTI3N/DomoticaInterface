package net.cs76.projects.student10340912.DomoticaInterface.DataManagement;

import android.content.Context;
import android.util.Log;

import net.cs76.projects.student10340912.DomoticaInterface.utils.Device;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Group;

import java.util.ArrayList;

/**
 * Created by sebastien on 14-10-14.
 */
public class DataManagerSingleton {

    public static final String SERVER_URL = "http://www.uvaatwork.nl/DomoticaInterface/";

    public static final int STATE_ON  = 1;
    public static final int STATE_OFF = 0;

    private static final String devicePropertyNames[] = {"Name", "Color", "State", "Notification"};
    private static final int devicePropertyIds[] = {0, 1, 2, 3, 4};

    public static final String MESSAGE_GROUP_ID = "MESSAGE_GROUP_ID";
    public static final String MESSAGE_DEVICE_ID = "MESSAGE_DEVICE_ID";

    private static volatile DataManagerSingleton instance = null;

    private DatabaseHelper database_ = null;
    private Context context_ = null;

    private DataManagerSingleton() {

    }

    public static DataManagerSingleton getInstance( Context context) {

        if (instance == null) {
            synchronized (DataManagerSingleton.class) {
                if (instance == null) {
                    instance = new DataManagerSingleton();
                    instance.setContext( context );
                    instance.initDatabase();
                }
            }
        }

        return instance;

    }

    public ArrayList<Group> getGroups() {
        return database_.getGroups();
    }

    public Group getGroupById( int groupId ) {
        return database_.getGroupById( groupId );
    }

    public ArrayList<Device> getDevicesByGroupId( int groupId ) {
        return database_.getDevicesByGroupId( groupId );
    }

    public Device getDeviceById( int id ) {
        return database_.getDeviceById( id );
    }

    public String[] getDevicePropertyNames(int deviceId) {
        return devicePropertyNames;
    }

    public int[] getDevicePropertyIds(int deviceId) {
        return devicePropertyIds;
    }

    private void initDatabase() {
        database_ = new DatabaseHelper( context_ );
    }

    public boolean isInitialised() {
        return database_.isInitialised();
    }

    private void setContext( Context context ) {
        context_ = context;
    }

    public void setProperty( int id, String param, Object value, Class type ) {

        String paramValue = "";
        if( value instanceof String  ) paramValue = ((String)value);
        else if( value instanceof Integer ) paramValue = value + "";


        if( type == Device.class ) {
            Log.d("DataManagerSingleton", "Switching param "+param+" for device " + id + " to " + paramValue);

            AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
            retriever.execute( SERVER_URL + "update.php" +
                    "?type=device_property&" +
                    "id="+id+"&" +
                    "property=" + param + "&" +
                    "value=" +paramValue );

            database_.setDeviceProperty(id, param, paramValue );

        }

    }
}