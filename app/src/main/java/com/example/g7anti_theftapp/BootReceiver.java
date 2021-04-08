package com.example.g7anti_theftapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = "TAG";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Opening BootReceiver class");
         if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
             Intent i = new Intent(context, SimChangedReceiver.class);
             //because we want to start activity outside our app we shall add this line
             i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(i);
         }
    }
}
