package com.example.g7anti_theftapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckAuthintication extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    DBHelper DB;
    Timer timer;
    public static  double latitude;
    public static  double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_authintication);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        DB = new DBHelper(this);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //SEND MAIL
                getLocation(getApplicationContext());
                Intent intent = new Intent(CheckAuthintication.this, lockScreen.class);
                startActivity(intent);
                finish();
            }
        }, 60000);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(CheckAuthintication.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    //SharedPreferences prefs = getSharedPreferences("SIM_State", MODE_PRIVATE);
                    String username = DB.getName();//prefs.getString("username", "00000000000");//"No name defined" is the default value.
                    String password =DB.getPassword(); //prefs.getString("password", "00000000000");//"No name defined" is the default value.
                    Log.d("Check",username);
                    Log.d("Check",password);
                    Boolean checkuserpass = user.equals(username) && password.equals(pass);
                    Log.d("Check", ""+checkuserpass);
                    if(checkuserpass==true){
                        Toast.makeText(CheckAuthintication.this, "changed SIM authenticate successfully", Toast.LENGTH_SHORT).show();

                        /*Re-enter data
                        String serialNumber = DB.getSerialNumber();
                        SharedPreferences.Editor editor = getSharedPreferences("SIM_State", MODE_PRIVATE).edit();
                        editor.putString("serialNumber", serialNumber);
                        editor.putString("username", user);
                        editor.putString("password", pass);
                        editor.apply();
                        */

                        //update serial number in DB
                        TelephonyManager telephoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String serialNumber = telephoneMgr.getSimSerialNumber();
                        DB.setSerialNumber(serialNumber, user);
                        IntentFilter intentFilter = new IntentFilter();
                        SimChangedReceiver simChangedReceiver = new SimChangedReceiver();
                        registerReceiver(simChangedReceiver, intentFilter);

                        timer.cancel();//--------> if someone enter pass corectlly then timer will be cancel

                        Intent intent  = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);
                        finish();
                    }else{
                        //SEND MAIL
                        getLocation(getApplicationContext());
                        timer.cancel();//--------> if someone enter pass wronge then timer will be cancel
                        Toast.makeText(CheckAuthintication.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CheckAuthintication.this, lockScreen.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void getLocation(Context context) {
        Log.d("SimStateListener", "inside method 1");
        try {

            LocationManager locationMangaer = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            Location location = locationMangaer.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("SimStateListener", "inside method 2 location" + location);
            Location loc = getLastKnownLocation(locationMangaer);
            locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

            Location location2 = locationMangaer.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("SimStateListener", "inside method 2 location" + location2);
            latitude = 0;
            longitude = 0;
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
            Log.d("SimStateListener", "inside method 2 location latitude" + latitude + "longitude"+longitude);


            String locationLink = "https://maps.google.com/?q="+ latitude+","+longitude;//nada
            Log.d("SimStateListener", "locationLink --> " +locationLink );
            sendMAilTo(locationLink);//nada


            boolean flag = displayGpsStatus(context);
            if (flag) {
                locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

            } else {
            }

        } catch (Exception e) {
            Log.d("SimStateListener", "inside method 2 location exception" + e.toString());
        }

    }

    private void sendMAilTo(String locationLink) {
        GMailSender.sendMail(DB.getName() , "Warning‼️",   " Your SIM card has been changed and this is the current location of your device "+ "\n\n"+locationLink);
        Toast.makeText(CheckAuthintication.this, " Email sent  ", Toast.LENGTH_LONG).show();
    }


    //nadafjj@gmail.com



    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus(Context context) {
        Log.d("SimStateListener", "inside method 2");

        ContentResolver contentResolver = context.getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    private Location getLastKnownLocation(LocationManager mLocationManager) {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestForSpecificPermission();
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            Log.d("SimStateListener","last known location, provider: %s, location: "+ provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                Log.d("SimStateListener","found best last known location:"+ l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }
    private void requestForSpecificPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    Log.d("Permission ","Accepet");
                } else {
                    //not granted
                    Log.d("Permission ","denied");

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}