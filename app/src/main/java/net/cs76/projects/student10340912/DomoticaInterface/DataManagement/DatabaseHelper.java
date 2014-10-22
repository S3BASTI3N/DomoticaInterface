package net.cs76.projects.student10340912.DomoticaInterface.DataManagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.cs76.projects.student10340912.DomoticaInterface.utils.DatabaseUpdatedInterface;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Device;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Group;
import net.cs76.projects.student10340912.DomoticaInterface.utils.LogicRule;
import net.cs76.projects.student10340912.DomoticaInterface.utils.LogicRuleStateDescriber;

import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 *  Http stream as suggested in:
 *  http://stackoverflow.com/questions/4457492/how-do-i-use-the-simple-http-client-in-android
 *
 */

// TODO all database writes could be done in an async task

/**
 * Created by sebastien on 14-10-14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "DomoticaInterface";

    Context context_;
    private long timestamp = 0;

    public DatabaseHelper( Context context ) {
        super( context, DB_NAME, null, 1 );

        Log.d("DatabaseHelper", "Init databasehelper" );

        context_ = context;

        getWritableDatabase();

    }

    public void updateDatabase() {
        AsyncServerHttpRetriever retriever =
                new AsyncServerHttpRetriever( this, AsyncServerHttpRetriever.REQUEST_DATABASE_UPDATE );
        retriever.execute( DataManagerSingleton.SERVER_URL + "updateDatabase.php?timestamp=" + timestamp );
        timestamp = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d( "DatabaseHelper", "Getting database from server" );
        AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever( this, AsyncServerHttpRetriever.REQUEST_DATABASE_INIT );
        retriever.execute( DataManagerSingleton.SERVER_URL + "getDatabase.php");

        Log.d( "DatabaseHelper", "Async started");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void callBack( String message, int type ) {
        if( message == null ) {
            return;
        }
        Log.d( "DatabaseHelper", message );

        SQLiteDatabase db_write = getWritableDatabase();
        String[] statements;

        switch( type ) {
            case AsyncServerHttpRetriever.REQUEST_DATABASE_INIT:
                Log.d( "DatabaseHelper", "Got back database init");

                statements = message.split(";");

                for( int i = 0; i < statements.length-1; i++ ) {
                    String statement = statements[i];
                    if( statement != "" ) {
                        Log.d("DatabaseHelper", "Executing statement: <" + statement + ">");
                        db_write.execSQL(statement);
                    }
                }

                updateDatabase();


                break;

            case AsyncServerHttpRetriever.REQUEST_DATABASE_UPDATE:
                Log.d( "DatabaseHelper", "Got back database update");

                statements = message.split(";");

                for( int i = 0; i < statements.length-1; i++ ) {
                    String statement = statements[i];
                    if( statement != "" ) {
                        Log.d("DatabaseHelper", "Executing statement: <" + statement + ">");
                        db_write.execSQL(statement);
                    }
                }

                if( context_ != null )
                    ((DatabaseUpdatedInterface)context_).onDatabaseUpdated();
                break;
        }

        db_write.close();
    }

    public ArrayList<Device> getDevicesByGroupId( int groupId ) {
        SQLiteDatabase db_read = getReadableDatabase();

        String query = "SELECT id, name, state, notification " +
                "FROM devices, relation_group_device " +
                "WHERE relation_group_device.group_id="+groupId + " " +
                "AND relation_group_device.device_id=id";

        Cursor cursor = db_read.rawQuery( query, null );

        Log.d( "test", query );

        cursor.moveToFirst();

        ArrayList<Device> devices = new ArrayList<Device>();

        while( !cursor.isAfterLast() ) {
            devices.add(new Device(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
            cursor.moveToNext();
        }

        db_read.close();

        return devices;
    }

    public ArrayList<Device> getDevicesNotFromGroup( int id ) {
        SQLiteDatabase db_read = getReadableDatabase();

        String query = "SELECT id, name, state, notification " +
                "FROM devices " +
                "WHERE NOT EXISTS (SELECT device_id "+
                "FROM relation_group_device "+
                "WHERE device_id=id " +
                "AND group_id="+id+")";


        Cursor cursor = db_read.rawQuery( query, null );

        Log.d( "test", query );

        cursor.moveToFirst();

        ArrayList<Device> devices = new ArrayList<Device>();

        while( !cursor.isAfterLast() ) {
            devices.add(new Device(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
            cursor.moveToNext();
        }

        db_read.close();

        return devices;
    }

    public void addGroup() {
        SQLiteDatabase dbWrite = getWritableDatabase();
        String query = "INSERT INTO groups( name ) VALUES( 'New Group' );";

        Log.d( "DatabaseHelper", "Add group query: " + query );

        dbWrite.execSQL( query );
    }

    public void addDeviceToGroup( int groupId, int deviceId ) {
        SQLiteDatabase dbWrite = getWritableDatabase();

        String query = "INSERT INTO relation_group_device( group_id, device_id ) VALUES( "+ groupId +", "+ deviceId +");";

        dbWrite.execSQL( query );
    }

    public void removeDeviceFromGroup( int groupId, int deviceId ) {
        SQLiteDatabase dbWrite = getWritableDatabase();

        String query = "DELETE FROM relation_group_device WHERE group_id="+ groupId +" AND device_id="+ deviceId;

        dbWrite.execSQL( query );
    }

    public void removeGroupById( int id ) {
        SQLiteDatabase dbWrite = getWritableDatabase();

        String query = "DELETE FROM groups WHERE id="+id;
        dbWrite.execSQL( query );

        query = "DELETE FROM relation_group_device WHERE group_id=" + id;
        dbWrite.execSQL( query );
    }

    public void removeLogicRuleById( int id ) {
        SQLiteDatabase dbWrite = getWritableDatabase();

        String query = "DELETE FROM logic_rules WHERE id="+id;
        dbWrite.execSQL( query );
    }

    public Device getDeviceById( int id ) {
        SQLiteDatabase db_read = getReadableDatabase();

        // TODO add menu_group id
        Cursor cursor = db_read.rawQuery( "SELECT id, name, state, notification FROM devices WHERE id="+id, null );
        cursor.moveToFirst();

        db_read.close();

        return new Device(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));

    }

    public ArrayList<Group> getGroups() {
        SQLiteDatabase db_read = getReadableDatabase();

        // TODO add menu_group id
        Cursor cursor = db_read.rawQuery( "SELECT id, name FROM groups", null );
        cursor.moveToFirst();

        ArrayList<Group> groups = new ArrayList<Group>();

        while( !cursor.isAfterLast() ) {
            groups.add(new Group(cursor.getInt(0), cursor.getString(1)));
            cursor.moveToNext();
        }

        db_read.close();

        return groups;

    }

    public Group getGroupById( int groupId ) {

        SQLiteDatabase dbRead = getReadableDatabase();

        Cursor cursor = dbRead.rawQuery( "SELECT id, name FROM groups WHERE id="+groupId, null );
        cursor.moveToFirst();

        dbRead.close();

        return new Group(cursor.getInt(0), cursor.getString(1));

    }

    public ArrayList<LogicRule> getLogicRules() {

        ArrayList<LogicRule> logicRules = new ArrayList<LogicRule>();

        SQLiteDatabase dbRead = getReadableDatabase();

        String query = "SELECT logic_rules.id, name, condition_id, device_id as condition_device_id, state as condition_state " +
                "FROM logic_rules, logic_rule_state_describers " +
                "WHERE logic_rules.condition_id = logic_rule_state_describers.id";

        Cursor cursor = dbRead.rawQuery( query, null );

        cursor.moveToFirst();

        while( !cursor.isAfterLast() ) {

            LogicRuleStateDescriber condition =
                    new LogicRuleStateDescriber( cursor.getInt(2), getDeviceById(cursor.getInt(3)), cursor.getInt(4) );

            String actionQuery = "SELECT logic_rule_state_describers.id, device_id, state " +
                    "FROM logic_rules, logic_rule_state_describers " +
                    "WHERE logic_rules.id=" + cursor.getInt(0) + " AND logic_rules.action_id=logic_rule_state_describers.id";

            Cursor actionCursor = getReadableDatabase().rawQuery(actionQuery, null);
            actionCursor.moveToFirst();

            LogicRuleStateDescriber action =
                    new LogicRuleStateDescriber( actionCursor.getInt(0), getDeviceById(actionCursor.getInt(1)), actionCursor.getInt(2) );

            LogicRule rule = new LogicRule( cursor.getInt(0), cursor.getString(1), condition, action );

            Log.d( "DatabaseHelper", "Got rule: " + rule );

            logicRules.add( rule );

            cursor.moveToNext();

        }

        return logicRules;
    }

    public LogicRule getLogicRuleById( int id ) {

        SQLiteDatabase dbRead = getReadableDatabase();

        String query = "SELECT logic_rules.id, name, condition_id, device_id as condition_device_id, state as condition_state " +
                "FROM logic_rules, logic_rule_state_describers " +
                "WHERE logic_rules.condition_id = logic_rule_state_describers.id AND logic_rules.id=" + id;

        Cursor cursor = dbRead.rawQuery(query, null);

        cursor.moveToFirst();

        LogicRuleStateDescriber condition =
                new LogicRuleStateDescriber(cursor.getInt(2), getDeviceById(cursor.getInt(3)), cursor.getInt(4));

        String actionQuery = "SELECT logic_rule_state_describers.id, device_id, state " +
                "FROM logic_rules, logic_rule_state_describers " +
                "WHERE logic_rules.id=" + cursor.getInt(0) + " AND logic_rules.action_id=logic_rule_state_describers.id";

        Cursor actionCursor = getReadableDatabase().rawQuery(actionQuery, null);
        actionCursor.moveToFirst();

        LogicRuleStateDescriber action =
                new LogicRuleStateDescriber(actionCursor.getInt(0), getDeviceById(actionCursor.getInt(1)), actionCursor.getInt(2));

        LogicRule rule = new LogicRule(cursor.getInt(0), cursor.getString(1), condition, action);

        Log.d("DatabaseHelper", "Got rule: " + rule);

        return rule;

    }

    public void addLogicRule() {

        SQLiteDatabase db_write = getWritableDatabase();

        int stateDescriber1 = addLogicRuleStateDescriber();
        int stateDescriber2 = addLogicRuleStateDescriber();

        String query = "INSERT INTO logic_rules( name, condition_id, action_id ) " +
                "VALUES( 'Name', "+stateDescriber1+" , "+stateDescriber2+" )";

        db_write.execSQL( query );

        db_write.close();

    }

    public int addLogicRuleStateDescriber() {
        SQLiteDatabase db_write = getWritableDatabase();

        String query = "INSERT INTO logic_rule_state_describers( device_id, state ) VALUES( 0, 0 )";
        db_write.execSQL( query );

        query = "SELECT id FROM logic_rule_state_describers ORDER BY id DESC";
        Cursor cursor = getReadableDatabase().rawQuery( query, null );
        cursor.moveToFirst();

        int id = cursor.getInt(0);

        AsyncServerHttpRetriever retriever = new AsyncServerHttpRetriever(  this, AsyncServerHttpRetriever.REQUEST_STATE_UPDATE );
        retriever.execute( DataManagerSingleton.SERVER_URL + "update.php" +
                "?type=logic_rule_state_describer&" +
                "id="+id+"&" +
                "property=add_rule&" );

        return id;
    }

    public ArrayList<Device> getDevices() {
        SQLiteDatabase db_read = getReadableDatabase();

        String query = "SELECT id, name, state, notification " +
                "FROM devices, relation_group_device " +
                "WHERE relation_group_device.device_id=id";

        Cursor cursor = db_read.rawQuery( query, null );

        cursor.moveToFirst();

        ArrayList<Device> devices = new ArrayList<Device>();

        while( !cursor.isAfterLast() ) {
            devices.add(new Device(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
            cursor.moveToNext();
        }

        db_read.close();

        return devices;
    }

    public void setDeviceProperty( int deviceId, String property, String value ) {

        SQLiteDatabase db_write = getWritableDatabase();

        String query = "UPDATE devices SET "+property+"='"+value+"' WHERE id=" +deviceId;

        db_write.execSQL( query );
        Log.d( "DatabaseHelper", "Updating state: " + query );

        db_write.close();

    }

    public void setGroupProperty( int groupId, String property, String value ) {

        SQLiteDatabase db_write = getWritableDatabase();

        String query = "UPDATE groups SET "+property+"='"+value+"' WHERE id=" +groupId;

        db_write.execSQL( query );
        Log.d( "DatabaseHelper", "Updating state: " + query );

        db_write.close();

    }

    public void setLogicRuleStateDesciberProperty( int id, String param, String paramValue ) {
        SQLiteDatabase dbWrite = getWritableDatabase();

        String query = "UPDATE logic_rule_state_describers SET "+param+"="+paramValue+" WHERE id=" + id;

        dbWrite.execSQL(query);
    }

    public boolean isInitialised() {
        SQLiteDatabase db_read = getReadableDatabase();
        Cursor cursor = db_read.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        cursor.moveToFirst();
        int size = 0;
        while( !cursor.isAfterLast() ) {
            size++;
            cursor.moveToNext();
        }
        Log.d( "DatabaseHelper", "Database table size: " + size );

        db_read.close();

        return size > 1;

    }
}
