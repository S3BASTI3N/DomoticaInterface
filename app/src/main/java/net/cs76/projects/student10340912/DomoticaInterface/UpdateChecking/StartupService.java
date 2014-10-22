package net.cs76.projects.student10340912.DomoticaInterface.UpdateChecking;

/**
 * Created by sebastien on 22-10-14.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class StartupService extends Service
{
    Alarm alarm = new Alarm();
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        alarm.SetAlarm(StartupService.this);
        return START_STICKY;
    }



    public void onStart(Context context,Intent intent, int startId)
    {
        alarm.SetAlarm(context);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}