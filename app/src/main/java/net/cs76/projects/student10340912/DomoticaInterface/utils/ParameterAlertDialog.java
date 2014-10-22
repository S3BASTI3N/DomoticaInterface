package net.cs76.projects.student10340912.DomoticaInterface.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.Device;
import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.Group;
import net.cs76.projects.student10340912.DomoticaInterface.ConfigurableObjects.LogicRuleStateDescriber;
import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;

import java.util.ArrayList;

/**
 * Created by sebastien on 15-10-14.
 */
public class ParameterAlertDialog extends AlertDialog.Builder {

    private DataManagerSingleton dataManager_ = null;
    private int deviceId_;
    private int groupId_;


    public static final int TYPE_DEVICE_NAME            = 0;
    public static final int TYPE_DEVICE_NOTIFICATION    = 1;
    public static final int TYPE_GROUP_NAME             = 2;
    public static final int TYPE_GROUP_ADD              = 3;
    public static final int TYPE_GROUP_REMOVE           = 4;
    public static final int TYPE_LOGIC_RULE_DEVICE      = 5;
    public static final int TYPE_LOGIC_RULE_STATES      = 6;

    public ParameterAlertDialog( final Context context, DataManagerSingleton dataManager, String defaultValue, Device device, int type ) {
        super( context );

        dataManager_ = dataManager;
        deviceId_ = device.id_;

        final EditText input;

        switch( type ) {
            case TYPE_DEVICE_NAME:

                setTitle("Change Device Name");
                // Set up the input
                input = new EditText(context);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                setView(input);
                input.setText( defaultValue );

                // Set up the buttons
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String return_text = input.getText().toString();

                        Log.d("DeviceActivity", "Got value: " + return_text);

                        dataManager_.setProperty(deviceId_, "name", return_text, Device.class);

                    }
                });
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                break;

            case TYPE_DEVICE_NOTIFICATION:

                setTitle("Change Notification");

                final RadioButton onButton = new RadioButton( context );
                final RadioButton offButton = new RadioButton( context );

                onButton.setTag( Device.NOTIFICATION_ON );
                offButton.setTag(Device.NOTIFICATION_OFF);

                if( device.notification_ == Device.NOTIFICATION_ON ) onButton.setChecked( true );
                else offButton.setChecked( true );

                onButton.setText("On");
                offButton.setText("Off");

                RadioGroup radioGroup = new RadioGroup( context );
                radioGroup.addView( onButton );
                radioGroup.addView( offButton );

                onButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ParameterAlertDialog", "Clicked on" );
                        offButton.setChecked(false);
                        onButton.setChecked( true );
                    }

                });

                offButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ParameterAlertDialog", "Clicked off" );
                        onButton.setChecked(false);
                        offButton.setChecked( true );
                    }

                });

                setView(radioGroup);


                // Set up the buttons
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int value = Device.NOTIFICATION_OFF;
                        if (onButton.isChecked()) {
                            Log.d("ParameterAlertDialog", "Switching notification to on");
                            value = Device.NOTIFICATION_ON;
                        } else {
                            Log.d("ParameterAlertDialog", "Switching notification to off");
                        }

                        dataManager_.setProperty(deviceId_, "notification", value, Device.class);

                    }
                });
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                break;

        }
    }

    public ParameterAlertDialog( final Context context, DataManagerSingleton dataManager, String defaultValue, Group group, int type ) {
        super( context );

        groupId_ = group.id_;
        dataManager_ = dataManager;

        final EditText input;
        final ArrayList<Device> devices;
        String[] deviceNames;

        switch( type ) {
            case TYPE_GROUP_NAME:
                setTitle("Change Group Name");
                // Set up the input
                input = new EditText(context);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                setView(input);
                input.setText( defaultValue );

                // Set up the buttons
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String return_text = input.getText().toString();

                        Log.d( "ParameterAlertDialog", "return text: " + return_text );

                        dataManager_.setProperty(groupId_, "name", return_text, Group.class);

                    }
                });
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                break;

            case TYPE_GROUP_ADD:
                setTitle("Add device");

                devices = dataManager.getDevicesNotInGroup(groupId_);
                deviceNames = new String[devices.size()];
                for( int i = 0; i < devices.size(); i++ ) {
                    deviceNames[i] = devices.get(i).name_;
                }

                // Set up the input
                setItems( deviceNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataManager_.addDeviceToGroup( groupId_, devices.get( which ).id_ );
                        ((DatabaseUpdatedInterface)context).onDatabaseUpdated();

                        dialog.dismiss();

                    }
                });

                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                break;

            case TYPE_GROUP_REMOVE:
                setTitle("Remove device");

                devices = dataManager.getDevicesByGroupId(groupId_);
                deviceNames = new String[devices.size()];
                for( int i = 0; i < devices.size(); i++ ) {
                    deviceNames[i] = devices.get(i).name_;
                }

                // Set up the input
                setItems( deviceNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataManager_.removeDeviceFromGroup( groupId_, devices.get( which ).id_ );
                        ((DatabaseUpdatedInterface)context).onDatabaseUpdated();

                        dialog.dismiss();

                    }
                });

                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                break;
        }

    }

    public ParameterAlertDialog( final Context context, DataManagerSingleton dataManager, String defaultValue, final LogicRuleStateDescriber stateDesc, int type ) {
        super( context );

        dataManager_ = dataManager;
        final ArrayList<Device> devices;
        final String deviceNames[];

        switch( type ) {
            case TYPE_LOGIC_RULE_DEVICE:

                setTitle("Change device");

                devices = dataManager.getDevices();
                deviceNames = new String[devices.size()];
                for (int i = 0; i < devices.size(); i++) {
                    deviceNames[i] = devices.get(i).name_;
                }

                // Set up the input
                setItems(deviceNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newDeviceId = devices.get(which).id_;
                        dataManager_.setProperty(stateDesc.id_, "device_id", newDeviceId, LogicRuleStateDescriber.class);
                        ((DatabaseUpdatedInterface) context).onDatabaseUpdated();

                        dialog.dismiss();

                    }
                });

                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                break;

            case TYPE_LOGIC_RULE_STATES:

                setTitle("Change State");

                final RadioButton onButton = new RadioButton( context );
                final RadioButton offButton = new RadioButton( context );

                onButton.setTag( Device.NOTIFICATION_ON );
                offButton.setTag(Device.NOTIFICATION_OFF);

                if( stateDesc.state_ == Device.NOTIFICATION_ON ) onButton.setChecked( true );
                else offButton.setChecked( true );

                onButton.setText("On");
                offButton.setText("Off");

                RadioGroup radioGroup = new RadioGroup( context );
                radioGroup.addView( onButton );
                radioGroup.addView( offButton );

                onButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ParameterAlertDialog", "Clicked on" );
                        offButton.setChecked(false);
                        onButton.setChecked( true );
                    }

                });

                offButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ParameterAlertDialog", "Clicked off" );
                        onButton.setChecked(false);
                        offButton.setChecked( true );
                    }

                });

                setView(radioGroup);


                // Set up the buttons
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int value = Device.NOTIFICATION_OFF;
                        if (onButton.isChecked()) {

                            value = Device.NOTIFICATION_ON;
                        }

                        dataManager_.setProperty(stateDesc.id_, "state", value, LogicRuleStateDescriber.class);

                    }
                });
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                break;
        }
    }
}
