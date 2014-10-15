package net.cs76.projects.student10340912.DomoticaInterface.DataManagement;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import net.cs76.projects.student10340912.DomoticaInterface.HomeActivity;
import net.cs76.projects.student10340912.DomoticaInterface.utils.CallBackInterface;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Device;
import net.cs76.projects.student10340912.DomoticaInterface.utils.Group;

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

    // TODO all writing to the database could be done in an Async task as well
    // TODO handle empty responses
    public void callBack( String message, int type ) {
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
                ((CallBackInterface)context_).onCallBack();
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

    public Device getDeviceById( int id ) {
        SQLiteDatabase db_read = getReadableDatabase();

        // TODO add group id
        Cursor cursor = db_read.rawQuery( "SELECT id, name, state, notification FROM devices WHERE id="+id, null );
        cursor.moveToFirst();

        db_read.close();

        return new Device(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));

    }

    public ArrayList<Group> getGroups() {
        SQLiteDatabase db_read = getReadableDatabase();

        // TODO add group id
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

        SQLiteDatabase db_read = getReadableDatabase();

        Cursor cursor = db_read.rawQuery( "SELECT id, name FROM groups WHERE id="+groupId, null );
        cursor.moveToFirst();

        db_read.close();

        return new Group(cursor.getInt(0), cursor.getString(1));

    }

    public void setDeviceProperty( int deviceId, String property, String value ) {

        SQLiteDatabase db_write = getWritableDatabase();

        String query = "UPDATE devices SET "+property+"='"+value+"' WHERE id=" +deviceId;

        db_write.execSQL( query );
        Log.d( "DatabaseHelper", "Updating state: " + query );

        db_write.close();

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
