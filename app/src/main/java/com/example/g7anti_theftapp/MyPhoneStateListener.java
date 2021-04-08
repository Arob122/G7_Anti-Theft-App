package com.example.g7anti_theftapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
        import android.util.Log;
        import android.webkit.WebView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.cast.CastRemoteDisplayLocalService.startService;
import static com.google.android.gms.cast.CastRemoteDisplayLocalService.stopService;

public class MyPhoneStateListener extends PhoneStateListener {

    public static Boolean phoneRinging = false;

    @Override
    public void onActiveDataSubscriptionIdChanged(int subId) {
        super.onActiveDataSubscriptionIdChanged(subId);
        Log.d("CheckService class ", " onActiveDataSubscriptionIdChanged YAY");
    }

    public MyPhoneStateListener() {
        super();
        Log.d("CheckService class ", " MyPhoneStateListener START");
    }


    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);
        Log.d("CheckService class ", " onServiceStateChanged");
    }




    public void onCallStateChanged(int state, String incomingNumber) {


        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d("DEBUG", "IDLE");
                phoneRinging = false;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d("DEBUG", "OFFHOOK");
                phoneRinging = false;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d("DEBUG", "RINGING");
                phoneRinging = true;

                break;
        }
    }

}