package net.cs76.projects.student10340912.DomoticaInterface.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import net.cs76.projects.student10340912.DomoticaInterface.DataManagement.DataManagerSingleton;

/**
 * Created by sebastien on 15-10-14.
 */
public class ParameterAlertDialog {

    private DataManagerSingleton dataManager_ = null;
    private int deviceId_;
    private AlertDialog.Builder builder_;

    public ParameterAlertDialog( Context context, DataManagerSingleton dataManager, String defaultValue, int deviceId ) {

        dataManager_ = dataManager;
        deviceId_ = deviceId;

        builder_ = new AlertDialog.Builder(context);
        builder_.setTitle("Change Property");

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder_.setView(input);
        input.setText( defaultValue );

        // Set up the buttons
        builder_.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String return_text = input.getText().toString();

                Log.d("DeviceActivity", "Got value: " + return_text);

                dataManager_.setProperty(deviceId_, "name", return_text, Device.class);
            }
        });
        builder_.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    public void show() {
        builder_.show();

        // TODO maybe set focus on input field and let the keyboard show up
    }
}
