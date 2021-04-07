package com.example.g7anti_theftapp;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/*----------Listener class to get coordinates ------------- */
public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {
        Log.d("Location Listener","Location changed : Lat: " + loc.getLatitude()+ " Lng: " + loc.getLongitude());
        String longitude = "Longitude: " +loc.getLongitude();
        Log.v("Location Listener", longitude);
        String latitude = "Latitude: " +loc.getLatitude();
        Log.v("Location Listener", latitude);

        /*----------to get City-Name from coordinates ------------- */
        String cityName=null;
        /*Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc
                    .getLongitude(), 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());
            cityName=addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        String s = longitude+"\n"+latitude +"\n\nMy Currrent City is: "+cityName;

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider,int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
