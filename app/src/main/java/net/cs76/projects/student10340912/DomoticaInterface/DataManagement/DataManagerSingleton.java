package net.cs76.projects.student10340912.DomoticaInterface.DataManagement;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import net.cs76.projects.student10340912.DomoticaInterface.utils.DatabaseUpdatedInterface;
import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.Device;
import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.Group;
import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.LogicRule;
import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.LogicRuleStateDescriber;

import java.util.ArrayList;

/**
 * Created by sebastien on 14-10-14.
 */
public class DataManagerSingleton {

    public static final String SERVER_URL = "http://www.uvaatwork.nl/DomoticaInterface/";

    public static final int STATE_ON  = 1;
    public static final int STATE_OFF = 0;

    private static final String devicePropertyNames[] = {"Name", "State", "Notification"};
    private static final int devicePropertyIds[] = {0, 1, 2, 3, 4};

    public static final String MESSAGE_GROUP_ID = "MESSAGE_GROUP_ID";
    public static final String MESSAGE_DEVICE_ID = "MESSAGE_DEVICE_ID";
    public static final String MESSAGE_LOGIC_RULE_ID = "MESSAGE_LOGIC_ID";

    private static volatile DataManagerSingleton instance = null;

    private DatabaseHelper database_ = null;
    private Context context_ = null;

    private Activity activity_;

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

    public ArrayList<LogicRule> getLogicRules() {
        return database_.getLogicRules();
    }

    public LogicRule getLogicRuleById( int id ) {
        return database_.getLogicRuleById( id );
    }

    public void setActiveActivity( Activity activity ) {

        this.activity_ = activity;

        //database_.updateDatabase();

    }

    public ArrayList<Group> getGroups() {
        return database_.getGroups();
    }

    public Group getGroupById( int groupId ) {
        return database_.getGroupById( groupId );
    }

    public void removeGroupById( int id ) {
        AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
        retriever.execute( SERVER_URL + "update.php" +
                "?type=group&" +
                "id="+id+"&" +
                "property=remove_group&" );

        database_.removeGroupById( id );
    }
    public ArrayList<Device> getDevicesByGroupId( int groupId ) {

        return database_.getDevicesByGroupId( groupId );
    }

    public void addLogicRule() {
        AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
        retriever.execute( SERVER_URL + "update.php" +
                "?type=logic_rule&" +
                "id="+-1+"&" +
                "property=add_rule&" );

        database_.addLogicRule();

        ((DatabaseUpdatedInterface)activity_).onDatabaseUpdated();
    }

    public ArrayList<Device> getDevicesNotInGroup( int id ) {
        return database_.getDevicesNotFromGroup( id );
    }

    public void addGroup() {

        AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
        retriever.execute( SERVER_URL + "update.php" +
                "?type=group&" +
                "id="+-1+"&" +
                "property=new_group&" );

        database_.addGroup();
    }

    public void addDeviceToGroup( int groupId, int deviceId ) {

        AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
        retriever.execute( SERVER_URL + "update.php" +
                "?type=group&" +
                "id="+groupId+"&" +
                "property=add_device&" +
                "value=" +deviceId );

        database_.addDeviceToGroup( groupId, deviceId );
    }

    public void removeDeviceFromGroup( int groupId, int deviceId ) {

        AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
        retriever.execute( SERVER_URL + "update.php" +
                "?type=group&" +
                "id="+groupId+"&" +
                "property=remove_device&" +
                "value=" +deviceId );

        database_.removeDeviceFromGroup( groupId, deviceId );
    }

    public Device getDeviceById( int id ) {
        return database_.getDeviceById( id );
    }

    public ArrayList<Device> getDevices() {
        return database_.getDevices();
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

    public void removeLogicRuleById( int id ) {
        AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
        retriever.execute( SERVER_URL + "update.php" +
                "?type=logic_rule&" +
                "id="+id+"&" +
                "property=remove&" );

        database_.removeLogicRuleById(id);
    }

    public void setProperty( int id, String param, Object value, Class type ) {

        String paramValue = "";
        if( value instanceof String  ) paramValue = ((String)value);
        else if( value instanceof Integer ) paramValue = value + "";


        if( type == Device.class ) {
            Log.d("DataManagerSingleton", "Switching param "+param+" for device " + id + " to " + paramValue);

            AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
            retriever.execute( SERVER_URL + "update.php" +
                    "?type=device&" +
                    "id="+id+"&" +
                    "property=" + param + "&" +
                    "value=" +paramValue );

            database_.setDeviceProperty(id, param, paramValue );

        }
        else if( type == Group.class ) {
            Log.d("DataManagerSingleton", "Switching param "+param+" for group " + id + " to " + paramValue);

            AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
            retriever.execute( SERVER_URL + "update.php" +
                    "?type=group&" +
                    "id="+id+"&" +
                    "property=" + param + "&" +
                    "value=" +paramValue );

            database_.setGroupProperty( id, param, paramValue );
        }

        else if( type == LogicRuleStateDescriber.class ) {
            Log.d("DataManagerSingleton", "Switching param "+param+" for state describer " + id + " to " + paramValue);

            AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( database_, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
            retriever.execute( SERVER_URL + "update.php" +
                    "?type=logic_rule_state_describer&" +
                    "id="+id+"&" +
                    "property=" + param + "&" +
                    "value=" +paramValue );

            database_.setLogicRuleStateDesciberProperty( id, param, paramValue );
        }

        if( activity_ != null )
            ((DatabaseUpdatedInterface)activity_).onDatabaseUpdated();

    }
}