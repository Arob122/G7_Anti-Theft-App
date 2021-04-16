package com.example.g7anti_theftapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class Email extends AppCompatActivity {
    Button sendEmail;
    public static  double latitude;
    public static  double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        sendEmail=findViewById(R.id.sendEmail);

        ///////////////////// Nada started here to add send mail code////////////////////
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  GMailSender.sendMail("nadafjj@gmail.com" , "Test",   " السلام عليكم ورحمة الله وبركاته "+ "\n\n"+"تم بحمد الله عملية ارسال الايميل ابشرك");
                Toast.makeText(Email.this, " Email sent  ", Toast.LENGTH_LONG).show();*/

               // getLocation(getApplicationContext());//هذا بس حطيته `هنا عشان اجرب
            }
        });

        // getLocation(getApplicationContext());

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


            String locationLink = "https://maps.google.com/?q=<"+ latitude+">,<"+longitude+">";//nada
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
        GMailSender.sendMail("Arob2604@gmail.com" , "Test",   " السلام عليكم ورحمة الله وبركاته "+ "\n\n"+locationLink);
        Toast.makeText(Email.this, " Email sent  ", Toast.LENGTH_LONG).show();
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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


}