package com.example.g7anti_theftapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Signup extends AppCompatActivity {
    private static final int BOOT_PERMISSION_CODE = 100;
    private static final int MY_PERMISSION_REQUEST_CODE_BOOT_COMPLETED =101 ;
    private static final int MY_PERMISSION_REQUEST_CODE_PHONE_STATE =100 ;
    private static final int PERMISSION_READ_STATE =123;


    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;
    String strPhoneType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermissionAndGetSIM();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        DB = new DBHelper(this);

        SharedPreferences prefs = getSharedPreferences("SIM_State", MODE_PRIVATE);
        String usernameOld = prefs.getString("username", "");//"No name defined" is the default value.
        String passwordOld = prefs.getString("password", "");//"No name defined" is the default value.
        boolean lock = prefs.getBoolean("lock", false);//"No name defined" is the default value.
        Log.d("Check", usernameOld);
        Log.d("Check", passwordOld);

        if (lock){
            Intent intent = new Intent(getApplicationContext(), lockScreen.class);
            startActivity(intent);
            finish();
        }
        /*if (!usernameOld.equals("") && !passwordOld.equals("")) {
            Toast.makeText(Signup.this, "The user already exist", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Homepage.class);
            startActivity(intent);
           // getLocation(getApplicationContext());
            finish();
        }*/


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals(""))
                    Toast.makeText(Signup.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {


                    if (pass.equals(repass)) {
                        Boolean checkuser = DB.checkusername(user);
                        if (checkuser == false) {
                            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String serialNumber = "";
                            try {
                                serialNumber = tManager.getSimSerialNumber();
                                Log.d("CheckService class", "Read " + serialNumber);
                            } catch (Exception e) {
                                Log.d("CheckService class", "Exception");
                            }
                            Boolean insert = DB.insertData(user, pass, serialNumber);
                            if (insert == true) {
                                Toast.makeText(Signup.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                                //Deem
                                SharedPreferences.Editor editor = getSharedPreferences("SIM_State", MODE_PRIVATE).edit();
                                editor.putString("serialNumber", serialNumber);
                                editor.putString("username", user);
                                editor.putString("password", pass);
                                editor.apply();
                                IntentFilter intentFilter = new IntentFilter();
                                SimChangedReceiver simChangedReceiver = new SimChangedReceiver();
                                registerReceiver(simChangedReceiver, intentFilter);
                                //end detecting
                                //Deem

                                //Nada
                                //asking permission
                              /* checkPermission(
                                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                                        BOOT_PERMISSION_CODE);*/
                                //Nada
                                //end permission


                                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Signup.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Signup.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Signup.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
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
            double latitude = 0;
            double longitude = 0;
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
            Log.d("SimStateListener", "inside method 2 location latitude" + latitude + "longitude"+longitude);


            boolean flag = displayGpsStatus(context);
            if (flag) {
                locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

            } else {
            }

        } catch (Exception e) {
            Log.d("SimStateListener", "inside method 2 location exception" + e.toString());
        }

    }

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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.



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





    //Nada's functions
    // Function to check and request permission.
   /*public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Signup.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(Signup.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(Signup.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

   @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == BOOT_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Signup.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(Signup.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }*/



    //Nada
    private void askPermissionAndGetSIM(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//23
            //check if we have READ_PHONE_STATE permission
            int readPhoneStatePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int readBootPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED);

            if (readBootPermission != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(new String[] {Manifest.permission.RECEIVE_BOOT_COMPLETED},
                        MY_PERMISSION_REQUEST_CODE_BOOT_COMPLETED);
            }

            if (readPhoneStatePermission!= PackageManager.PERMISSION_GRANTED ){
                //if there is no permission, prompt the user to allow
                this.requestPermissions(new String[] {Manifest.permission.RECEIVE_BOOT_COMPLETED},
                        MY_PERMISSION_REQUEST_CODE_PHONE_STATE);
                return;
            }
        }
       //
        getSIM();
    }


    private String getSIM() {

        Log.d("CheckService class", "start Method");
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String serialNumber = "";
        //TelephonyManager.registerPhoneStateListener
        try {

            serialNumber = tManager.getSimSerialNumber();
            Log.d("CheckService class", "Read " + serialNumber);
        } catch (Exception e) {
            Log.d("CheckService class", "Exception");
        }

        return serialNumber;
    }


}

