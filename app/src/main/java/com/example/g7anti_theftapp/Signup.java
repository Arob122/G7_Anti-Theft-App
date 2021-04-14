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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    private static final int BOOT_PERMISSION_CODE = 100;
    private static final int MY_PERMISSION_REQUEST_CODE_BOOT_COMPLETED =101 ;
    private static final int MY_PERMISSION_REQUEST_CODE_PHONE_STATE =100 ;
    private static final int PERMISSION_READ_STATE =123;


    EditText Email, password, repassword;
    Button signup, signin;
    DBHelper DB;
    String strPhoneType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //askPermissionAndGetSIM();

        Email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        DB = new DBHelper(this);


        //Check Permission
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }

        //End Checking permission


        /*DB.getName();
        DB.getPassword();
        DB.getSerialNumber();
        DB.getUserStatus();*/
        SharedPreferences prefs = getSharedPreferences("SIM_State", MODE_PRIVATE);
        String EmailOld = DB.getName();//prefs.getString("username", "");//"No name defined" is the default value.
        String passwordOld = DB.getPassword();//prefs.getString("password", "");//"No name defined" is the default value.
        boolean lock = DB.getUserStatus().equals("lock");//prefs.getBoolean("lock", false);//"No name defined" is the default value.
        Log.d("Check", EmailOld);
        Log.d("Check", passwordOld);

        if (lock){
            Intent intent = new Intent(getApplicationContext(), lockScreen.class);
            startActivity(intent);
            finish();
        }

        if (!EmailOld.equals("") && !passwordOld.equals("")) {
            Toast.makeText(Signup.this, "The user already exist", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Homepage.class);
            startActivity(intent);
            getLocation(getApplicationContext());
            finish();
        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = Email.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();


                if (user.equals("") || pass.equals("") || repass.equals(""))
                    Toast.makeText(Signup.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                if (pass.length()<=7){
                    password.setError("Password must be greater than 7 characters");

                    return;
                }
                else {


                    if (pass.equals(repass)) {
                        if(!isEmailValid(user)){
                            Email.setError("The email address is badly formatted");
                        return;}


                        Boolean checkuser = DB.checkEmail(user);
                        if (checkuser == false) {
                            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String serialNumber = "";
                            try {
                                serialNumber = tManager.getSimSerialNumber();
                                Log.d("CheckService class", "Read " + serialNumber);
                            } catch (Exception e) {
                                Log.d("CheckService class", "Exception");
                            }
                            Boolean insert = DB.insertData(user, pass, serialNumber,"notlock");
                            if (insert == true) {
                                Toast.makeText(Signup.this, "Registered successfully", Toast.LENGTH_SHORT).show();


                                IntentFilter intentFilter = new IntentFilter();
                                SimChangedReceiver simChangedReceiver = new SimChangedReceiver();
                                registerReceiver(simChangedReceiver, intentFilter);

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
    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

    private void requestForSpecificPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.BROADCAST_STICKY, Manifest.permission.REBOOT, Manifest.permission.READ_PHONE_STATE}, 101);
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission ","true");
            return true;
        } else {
            Log.d("Permission ","false");
            return false;
        }
    }
}