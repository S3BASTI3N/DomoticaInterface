package net.cs76.projects.student10340912.DomoticaInterface.UpdateChecking;

/**
 * Created by sebastien on 22-10-14.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver
{
    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.SetAlarm(context);
        }
    }
}
