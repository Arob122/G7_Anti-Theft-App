package com.example.g7anti_theftapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;

public class checkSimCard extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = getSharedPreferences("SIM_State", MODE_PRIVATE);
        String SIM_Number = prefs.getString("serialNumber", "00000000000");//"No name defined" is the default value.
        Log.d("CheckService class","Base "+ SIM_Number);
        String newNumber= getPhoneNumber();
        if (!SIM_Number.equals(newNumber)){
            Log.d("CheckService class","Changed");
        }
        else
            Log.d("CheckService class","Same");
        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getPhoneNumber(){
        Log.d("CheckService class","start Method");
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String serialNumber_New ="";
        //TelephonyManager.registerPhoneStateListener
        try {

            serialNumber_New = tManager.getSimSerialNumber();
            Log.d("CheckService class","Read "+ serialNumber_New);
        } catch (Exception e) {
            Log.d("CheckService class","Exception");
        }

        return serialNumber_New;
    }


}
