package com.example.g7anti_theftapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class SimChangedReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equalsIgnoreCase("android.intent.action.SIM_STATE_CHANGED")) {
            Log.d("SimChangedReceiver", "--> SIM state changed <--");
        }
        // Checks Sim card State
        Log.d("SimStateListener", "Enter class");

        TelephonyManager telephoneMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephoneMgr.getSimState();

        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                Log.d("SimStateListener", "Sim State absent");
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                Log.d("SimStateListener", "Sim State network locked");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                Log.d("SimStateListener", "Sim State pin required");
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                Log.d("SimStateListener", "Sim State puk required");
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                Log.d("SimStateListener", "Sim State unknown");
                break;
            case TelephonyManager.SIM_STATE_READY:
                Log.d("SimStateListener", "Sim State ready");
                String serialNumber_New = telephoneMgr.getSimSerialNumber();
                Log.d("SimStateListener", serialNumber_New);
                Toast.makeText(context, serialNumber_New, Toast.LENGTH_LONG).show();

                //getLocation(context);


                SharedPreferences prefs = context.getSharedPreferences("SIM_State", MODE_PRIVATE);
                String SIM_Number = prefs.getString("serialNumber", "00000000000");//"No name defined" is the default value.
                Log.d("SimStateListener","Base "+ SIM_Number);
                if (!SIM_Number.equals(serialNumber_New)){
                    Log.d("SimStateListener","Changed");
                    Toast.makeText(context, "تم ملاحظة حالة غير طبيعية يرجى إعادة التسجيل", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, CheckAuthintication.class));
                }
                else
                    Log.d("SimStateListener","Same");


        }
    }

}

